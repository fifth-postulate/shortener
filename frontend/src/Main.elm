module Main exposing (..)

import Browser
import Html exposing (Html)
import Html.Attributes as Attribute
import Html.Events as Event
import Http
import Json.Decode as Decode exposing (Decoder)
import Json.Encode as Encode


main : Program () Model Msg
main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }


init : () -> ( Model, Cmd Msg )
init _ =
    ( NotAskedYet { server_url = "http://localhost:7478", url = "" }, Cmd.none )


type Model
    = FlowBroke
    | NotAskedYet ShortCommand
    | Shortening
    | Failure Http.Error
    | Success ShortenOutcome


type alias ShortCommand =
    { server_url : String
    , url : String
    }


type ShortenOutcome
    = Shortened DataSheet
    | Problem String


type alias DataSheet =
    { short : String
    , url : String
    }


view : Model -> Html Msg
view model =
    case model of
        NotAskedYet command ->
            askUrlView command

        Shortening ->
            shorteningView

        Failure _ ->
            whoops "Problems with retrieving a response"

        Success (Shortened sheet) ->
            successView sheet

        Success (Problem reason) ->
            whoops reason

        FlowBroke ->
            whoops "Illegal flow state occured"


askUrlView : ShortCommand -> Html Msg
askUrlView _ =
    Html.div []
        [ Html.input
            [ Attribute.type_ "text"
            , Attribute.placeholder "Enter an URL"
            , Attribute.name "url"
            , Event.onInput InputUpdated
            ]
            []
        , Html.button [ Event.onClick Shorten ] [ Html.text "Shorten" ]
        ]


shorteningView : Html msg
shorteningView =
    Html.div [] [ Html.text "url is being shortened" ]


whoops : String -> Html msg
whoops explanation =
    Html.div []
        [ Html.p [] [ Html.text "Something went wrong" ]
        , Html.p [] [ Html.text explanation ]
        ]


successView : DataSheet -> Html Msg
successView sheet =
    Html.div []
        [ Html.p [] [ Html.text <| sheet.url ++ " is shortened to" ]
        , Html.p [] [ Html.text sheet.short ]
        ]


type Msg
    = InputUpdated String
    | Shorten
    | Received (Result Http.Error ShortenOutcome)


update : Msg -> Model -> ( Model, Cmd Msg )
update message model =
    case ( message, model ) of
        ( InputUpdated url, NotAskedYet command ) ->
            ( NotAskedYet { command | url = url }, Cmd.none )

        ( Shorten, NotAskedYet command ) ->
            let
                cmd =
                    Http.post
                        { url = command.server_url
                        , body = Http.jsonBody <| encode command
                        , expect = Http.expectJson Received outcomeDecoder
                        }
            in
            ( Shortening, cmd )

        ( Received result, Shortening ) ->
            case result of
                Ok outcome ->
                    ( Success <| outcome, Cmd.none )

                Err reason ->
                    ( Failure reason, Cmd.none )

        _ ->
            ( FlowBroke, Cmd.none )


subscriptions : Model -> Sub Msg
subscriptions _ =
    Sub.none


encode : ShortCommand -> Encode.Value
encode command =
    Encode.object [ ( "url", Encode.string command.url ) ]


outcomeDecoder : Decoder ShortenOutcome
outcomeDecoder =
    Decode.field "type" Decode.string
        |> Decode.andThen decoderSwitch


decoderSwitch : String -> Decoder ShortenOutcome
decoderSwitch outcomeType =
    case outcomeType of
        "success" ->
            Decode.field "data" <| Decode.map Shortened sheetDecoder

        _ ->
            Decode.map Problem problemDecoder


sheetDecoder : Decoder DataSheet
sheetDecoder =
    Decode.map2 DataSheet
        (Decode.field "short" Decode.string)
        (Decode.field "url" Decode.string)


problemDecoder : Decoder String
problemDecoder =
    Decode.field "reason" Decode.string
