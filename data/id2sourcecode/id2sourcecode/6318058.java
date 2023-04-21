    protected void checkDiscardCache() {
        if (i_readOffset == i_writeOffset && i_cache.length > DISCARD_BUFFER_SIZE) {
            i_cache = null;
            i_readOffset = 0;
            i_writeOffset = 0;
        }
    }
