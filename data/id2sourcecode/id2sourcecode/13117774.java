    private void adjustCache() throws IOException {
        if (pageSet.size() > maxPage) {
            final Iterator<Long> iterator = pageSet.iterator();
            final long removedPosition = iterator.next();
            iterator.remove();
            if (writeCacheMap.containsKey(removedPosition)) {
                randomAccessFileDelegate.seek(removedPosition);
                final byte[] page = writeCacheMap.get(removedPosition);
                randomAccessFileDelegate.write(page);
                writeCacheMap.remove(removedPosition);
                if (_log.isDebugEnabled()) {
                    _log.debug("remove to write");
                }
            } else {
                if (readCacheMap.remove(removedPosition) == null) {
                    throw new IOException("page " + removedPosition + " must be in read or write cache");
                }
            }
        }
    }
