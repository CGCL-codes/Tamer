    public DecodeResult decode(FileScannerInput parentInput, long position, Decoder decoder, String inputName) throws IOException {
        final CacheFile cacheFile = acquireCacheFile();
        CacheMapEntry cacheEntry = null;
        IOException lastIOError = null;
        try {
            final FileChannel cache = cacheFile.getCache();
            final DecoderContext decoderContext = new DecoderContext(decoder, parentInput, position, parentInput.size());
            final long entryStart = cache.size();
            long entryEnd = entryStart;
            entryEnd += cache.transferFrom(decoderContext, entryStart, Long.MAX_VALUE);
            cacheEntry = new CacheMapEntry(cache, entryStart, entryEnd);
            decoderContext.validate();
            lastIOError = decoderContext.lastIOError();
        } catch (final IOException e) {
            lastIOError = e;
        } finally {
            releaseCache(cacheFile);
        }
        if (cacheEntry == null || lastIOError instanceof ClosedChannelException) {
            throw lastIOError;
        }
        if (lastIOError != null) {
            logIOException(inputName, lastIOError);
        }
        final FileScannerInput decodedInput = new DecodedFileScannerInput(parentInput, inputName, cacheEntry, lastIOError);
        return new DecodeResult(decodedInput, position + decoder.totalIn());
    }
