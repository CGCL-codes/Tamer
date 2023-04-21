    private void encode_and_send_tailsegment() {
        logger.info("(SI={}): encode segment {}/{}", new Object[] { storageIndex, segment_num + 1, num_segments });
        start_encoding = System.nanoTime();
        SHA256d crypttext_segment_hasher = Hasher.getCrypttextSegmenthasher();
        ByteArray[] chunks = _gather_data(encodingParam.getK(), last_block_size, crypttext_segment_hasher, true);
        ByteArray hash = new ByteArray(crypttext_segment_hasher.digest());
        cryptext_hashes.add(hash);
        logger.debug("(SI={}): trigger encode for segment {}", storageIndex, segment_num);
        trigger(new Encode(encodingParam.getK(), encodingParam.getN(), chunks), redundancy);
    }
