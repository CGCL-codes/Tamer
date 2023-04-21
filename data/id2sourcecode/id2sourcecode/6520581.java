    public Resource getResource(String name) {
        try {
            URL url = new URL(baseURL, name, cache.get(factory, protocol));
            URLConnection connection = url.openConnection();
            int length = connection.getContentLength();
            InputStream stream = connection.getInputStream();
            if (connection instanceof HttpURLConnection) {
                int response = ((HttpURLConnection) connection).getResponseCode();
                if (response / 100 != 2) return null;
            }
            if (stream != null) return new RemoteResource(this, name, url, stream, length); else return null;
        } catch (IOException ioe) {
            return null;
        }
    }
