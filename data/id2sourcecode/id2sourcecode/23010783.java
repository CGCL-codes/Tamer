    public WritableSSKFileURI(byte[] writekey, byte[] fingerprint) {
        this.writekey = writekey;
        this.readkey = Hash.ssk_readkey_hash(writekey);
        this.storageIndex = Hash.ssk_storage_index_hash(readkey);
        this.fingerprint = fingerprint;
    }
