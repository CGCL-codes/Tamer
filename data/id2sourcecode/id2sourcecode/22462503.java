    public DecodeResult decodeMap(FileScannerInput parentInput, DecodeMap map, String inputName) throws IOException {
        final CacheFile cacheFile = acquireCacheFile();
        final ArrayList<MapEntry> cacheEntries = new ArrayList<MapEntry>(map.getEntryCount());
        IOException lastIOError = null;
        long decodeExtent = 0;
        try {
            final FileChannel cache = cacheFile.getCache();
            final DecodeMap.Entry[] mapEntries = map.getEntries();
            for (final DecodeMap.Entry mapEntry : mapEntries) {
                final long mapEntryStart = mapEntry.getStart();
                final long mapEntryEnd = mapEntry.getEnd();
                final Decoder decoder = mapEntry.getDecoder();
                if (decoder != null) {
                    final long decoderTotalInStart = decoder.totalIn();
                    DecoderContext decoderContext;
                    if (mapEntryEnd > mapEntryStart) {
                        decoderContext = new DecoderContext(decoder, parentInput, mapEntryStart, mapEntryEnd);
                    } else {
                        decoderContext = new DecoderContext(decoder, parentInput, mapEntryStart);
                    }
                    final long entryStart = cache.size();
                    final long entryEnd = entryStart + cache.transferFrom(decoderContext, entryStart, Long.MAX_VALUE);
                    cacheEntries.add(new CacheMapEntry(cache, entryStart, entryEnd));
                    decoderContext.validate();
                    lastIOError = decoderContext.lastIOError();
                    final long decoderIn = decoder.totalIn() - decoderTotalInStart;
                    if (mapEntryStart < mapEntryEnd) {
                        decodeExtent = Math.max(decodeExtent, mapEntryEnd + mapEntry.getPadding());
                    } else {
                        decodeExtent = Math.max(decodeExtent, mapEntryStart + decoderIn);
                    }
                } else {
                    cacheEntries.add(new InputMapEntry(parentInput, mapEntryStart, mapEntryEnd));
                    decodeExtent = Math.max(decodeExtent, mapEntryEnd + mapEntry.getPadding());
                }
                if (lastIOError != null) {
                    throw lastIOError;
                }
            }
        } catch (final IOException e) {
            lastIOError = e;
        } finally {
            releaseCache(cacheFile);
        }
        if (cacheEntries.size() == 0 || lastIOError instanceof ClosedChannelException) {
            throw lastIOError;
        }
        if (lastIOError != null) {
            logIOException(inputName, lastIOError);
        }
        final FileScannerInput decodedInput = new DecodeMappedFileScannerInput(parentInput, inputName, cacheEntries, lastIOError);
        return new DecodeResult(decodedInput, decodeExtent);
    }
