function(doc) {
    if (doc.type === 'creation') {
        emit(doc.short, { type: doc.type, url: doc.url });
    }
    if (doc.type === 'retrieve') {
        emit(doc.short, { type: doc.type });
    }
}