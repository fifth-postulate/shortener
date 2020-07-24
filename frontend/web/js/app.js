(function () {
    var node = document.getElementById('container');
    var app = Elm.Main.init({
        node: node,
        flags: server.url
    });
})();