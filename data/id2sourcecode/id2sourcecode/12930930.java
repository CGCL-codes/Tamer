    private void refreshCacheFile() throws IOException {
        lastError = null;
        if (System.currentTimeMillis() - lastTryCacheRefresh < 60000) return;
        lastTryCacheRefresh = System.currentTimeMillis();
        initCacheFile();
        try {
            HttpURLConnection ct = (HttpURLConnection) new URL(ECB_RATES_URL).openConnection(proxy);
            int errorCode = ct.getResponseCode();
            if (errorCode == HttpURLConnection.HTTP_OK) {
                InputStreamReader in = new InputStreamReader(ct.getInputStream());
                try {
                    Writer out = new OutputStreamWriter(FileUtils.getHiddenCompliantStream(cacheFile));
                    try {
                        int c;
                        while ((c = in.read()) != -1) out.write(c);
                    } catch (IOException e) {
                        lastError = "Read/Write Error: " + e.getMessage();
                    } finally {
                        out.flush();
                        out.close();
                    }
                } finally {
                    in.close();
                }
            } else {
                throw new IOException("Http Error " + errorCode);
            }
        } catch (IOException e) {
            lastError = "Connection/Open Error: " + e.getMessage();
        }
        if (lastError != null) {
            throw new IOException(lastError);
        }
    }
