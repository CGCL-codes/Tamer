    public synchronized URLConnection openConnection(URL url, Proxy p) throws IOException {
        String path;
        String file = url.getFile();
        String host = url.getHost();
        path = file;
        if ((host == null) || host.equals("") || host.equalsIgnoreCase("localhost") || host.equals("~")) {
            return createFileURLConnection(url, new File(path));
        }
        throw new IOException("localfile only supports localhost");
    }
