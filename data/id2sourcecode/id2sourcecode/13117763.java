    private void writeModified() throws IOException {
        if (_log.isDebugEnabled()) {
            _log.debug("to write=" + writeCacheMap.size() + ", readed=" + readCacheMap.size() + " " + (new Date()));
        }
        final Iterator<Long> iterator = writeCacheMap.keySet().iterator();
        long previousPagePositionInfile = Long.MIN_VALUE;
        while (iterator.hasNext()) {
            final long pagePositionInfile = iterator.next();
            if (previousPagePositionInfile + pageSize != pagePositionInfile) {
                randomAccessFileDelegate.seek(pagePositionInfile);
            }
            final byte[] page = writeCacheMap.get(pagePositionInfile);
            if (page.length != pageSize) {
                throw new IOException("data size is " + page.length + " for " + pageSize + " expected");
            }
            randomAccessFileDelegate.write(page);
            if (haveReadCache) {
                readCacheMap.put(pagePositionInfile, page);
            } else {
                pageSet.remove(pagePositionInfile);
            }
            previousPagePositionInfile = pagePositionInfile;
        }
        writeCacheMap.clear();
        if (_log.isDebugEnabled()) {
            _log.debug("readed=" + readCacheMap.size() + " " + (new Date()));
        }
    }
