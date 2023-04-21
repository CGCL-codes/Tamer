    private void finish_hashing() {
        start_hashing = System.nanoTime();
        byte[] crypttexthash = crypttext_hasher.digest();
        logger.debug("(SI={}): crypttextHash={}", storageIndex, Base32.encode(crypttexthash));
        uriExtensionData.addKeyValue("crypttext_hash", crypttexthash);
        try {
            uploadable.close();
        } catch (IOException e) {
            logger.error("Exception while closing the file: ", e);
        }
    }
