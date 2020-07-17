module Main exposing (..)

import Browser
import Debug
import Html exposing (Html)
import Html.Attributes as Attribute
import Html.Events as Event


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
    ( { url = "" }, Cmd.none )


type alias Model =
    { url : String }


view : Model -> Html Msg
view model =
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


type Msg
    = InputUpdated String
    | Shorten


update : Msg -> Model -> ( Model, Cmd Msg )
update message model =
    case message of
        InputUpdated url ->
            ( { model | url = url }, Cmd.none )

        Shorten ->
            ( Debug.log "shorten" model, Cmd.none )


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none
