    private void readToBufferCached() throws IOException {
        if (fpos != 0 && fpos < 2100 && fnamp.endsWith("sepl_18.se1")) {
            load(fpos, 5 * 300);
        }
        long idx = fpos / bufsize;
        boolean isError = false;
        CacheEntry ce;
        String debugInfo = null;
        synchronized (cache) {
            ce = cache.get(idx);
            if (debugOutput) debugInfo = cache.toString();
        }
        if (ce != null) {
            synchronized (ce) {
                while (ce.isBusy) {
                    try {
                        if (debugOutput) System.err.format("WAIT: %5d %s\n", idx, debugInfo);
                        ce.wait();
                        isError = ce.isError;
                    } catch (InterruptedException ex) {
                        isError = true;
                    }
                }
            }
            if (isError) {
                synchronized (cache) {
                    cache.remove(idx);
                }
                ce = null;
            }
        }
        if (ce != null) {
            if (debugOutput) System.err.format("HIT: %5d %10d %s %s\n", idx, fpos, fnamp, cache.toString());
            startIdx = ce.startIdx;
            endIdx = ce.endIdx;
            data = ce.data;
            return;
        }
        isError = false;
        if (debugOutput) System.err.format("READ: %5d %10d %s %s\n", idx, fpos, fnamp, cache.toString());
        urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setRequestProperty("User-Agent", useragent);
        long offsetForGet = idx * bufsize;
        urlCon.setRequestProperty("Range", "bytes=" + offsetForGet + "-" + SMath.min(length() - 1, offsetForGet + bufsize - 1));
        urlCon.connect();
        int rc = urlCon.getResponseCode();
        int contentLength = urlCon.getHeaderFieldInt("Content-Length", -1);
        int length = -1;
        if (rc != HttpURLConnection.HTTP_OK && rc != HttpURLConnection.HTTP_PARTIAL) {
            isError = true;
        } else {
            InputStream is = urlCon.getInputStream();
            byte[] newData = new byte[bufsize];
            length = is.read(newData);
            if (is.available() > 0 || length > bufsize || (length < bufsize && savedLength >= 0 && offsetForGet + length != savedLength)) {
                isError = true;
            } else {
                startIdx = offsetForGet;
                endIdx = offsetForGet + length;
                ce = new CacheEntry(idx, startIdx, endIdx);
                ce.data = newData;
                data = newData;
                synchronized (cache) {
                    cache.put(idx, ce);
                }
            }
        }
        if (isError) {
            throw new IOException("HTTP read failed with HTTP response " + rc + ". Read " + length + " (" + contentLength + ") bytes, requested " + bufsize + " bytes.");
        }
    }
