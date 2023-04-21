    public WritableSSKFileURI(String cap) {
        Matcher matcher = pattern.matcher(cap);
        if (matcher.matches()) {
            this.writekey = Base32.decode(matcher.group(1));
            this.readkey = Hash.ssk_readkey_hash(writekey);
            this.storageIndex = Hash.ssk_storage_index_hash(readkey);
            this.fingerprint = Base32.decode(matcher.group(2));
        } else {
            writekey = null;
            readkey = null;
            storageIndex = null;
            fingerprint = null;
        }
    }
