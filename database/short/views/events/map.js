function(doc) {
    if (doc.type === 'created') {
        emit(doc.short, { type: doc.type, url: doc.url });
    }
    if (doc.type === 'retrieved') {
        emit(doc.short, { type: doc.type });
    }
}