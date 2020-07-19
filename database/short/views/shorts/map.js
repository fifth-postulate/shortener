function(doc){
    if (doc._id.startsWith('claim:')) {
        emit(doc._id.split(':')[1], 1);
    }
}