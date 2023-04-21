    public KeyStoreOperations(Storage localStorage, JsonReader<KeySet> keyset_reader, JsonWriter<KeySet> keyset_writer) {
        this.localStorage = localStorage;
        this.keyset_reader = keyset_reader;
        this.keyset_writer = keyset_writer;
    }
