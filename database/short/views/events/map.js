function(doc) {
    if (doc.type === 'creation') {
        emit([doc.short, doc.type], { type: doc.type, url: doc.url });
    }
    if (doc.type === 'retrieve') {
        emit([doc.short, doc.type], 1);
    }
}