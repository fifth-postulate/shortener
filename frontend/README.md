# URL Shortener Frontend
This is the frontend for the URL Shortener.

It is build in [Elm][elm].

## Development
### Building
There is a `Makefile` that builds all the artifacts. The `web` folder can be considered as a unit of deployment.

### Running
To run the application, make sure to have run the `make` command

```
docker run -d --rm --name shortener_frontend -p 8000:80 dvberkel/shortener_frontend
```

## Deployment
The frontend application needs to know how which server to send the shorten commands to. This can be configured by changing the file `/usr/share/nginx/html/js/server/url.js` with the something like

```js
server = { 'url': 'http://localhost:7478' };
```

This can be achieved with a `ConfigMap`, and mounting it as a volume.

[elm]: https://elm-lang.org/
[k8s:configmap]: https://kubernetes.io/docs/concepts/configuration/configmap/