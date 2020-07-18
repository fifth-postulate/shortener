# Setup
This document describes the steps that went into setting up the database.

I am familiar with [CouchApp][couchapp]. This is a python app specifically written for Python 2, which is End-Of-Life.
Besides that, it also can't handle defining users, as can be seen from [this issue][couchapp:issue]. So I wanted to look at other tools and found [jo's best practices][jo].

## Tools
I started installing the tools with the following command

```plain
> npm install --global couchdb-bootstrap couchdb-compile couchdb-configure couchdb-ensure couchdb-push couchdb-secure
```

## Database
I create a database for my local CouchDB instance with the `couchdb-ensure` command

```plain
> couchdb-ensure http://admin:password@localhost:5984/shortener
```

which responded with

```json
{
  "ok": true
}
```

## Compile Design Document
Next a created a directory stucture adhering [the couchapp structure][structure].

```plain
├── short
    ├── _id
    └── views
        └── events
            └── map.js
```

and used `couchdb-compile short` and got the following output

```json
{
  "_id": "_design/short",
  "views": {
    "events": {
      "map": "function(doc) {\n    if (doc.type === 'creation') {\n        emit([doc.short, doc.type], { type: doc.type, url: doc.url });\n    }\n    if (doc.type === 'retrieve') {\n        emit([doc.short, doc.type], 1);\n    }\n}"
    }
  }
}
```

## Pushing to the database
The previous step was not necessary since the `couchdb-push` command can also push the same directory structure. I did end up using the compiled design document.

```plain
couchdb-push http://admin:password@localhost:5984/shortener short_ddoc.json
```

Which returned

```json
{
  "ok": true,
  "id": "_design/short",
  "rev": "1-c16243e9fc0b4761b20444ecc00a23e7"
}
```

[couchapp]: https://github.com/couchapp/couchapp
[couchapp:issue]: https://github.com/couchapp/couchapp/issues/126
[jo]: https://github.com/jo/couchdb-best-practices
[structure]: https://github.com/couchapp/couchapp/wiki/Complete-Filesystem-to-Design-Doc-Mapping-Example