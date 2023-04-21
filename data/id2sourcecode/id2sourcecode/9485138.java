    public void loadFile() throws IOException {
        StreamInfo info = new StreamInfo(extension2type(url), url.toString());
        InputStream is = url.openStream();
        if (url.getFile().toLowerCase().endsWith(".gz")) is = new GZIPInputStream(is); else is = new BufferedInputStream(is);
        if (!threaded) {
            notifyListeners(is, info);
        } else {
            new BackgroundFileLoader(is, info).start();
        }
    }
