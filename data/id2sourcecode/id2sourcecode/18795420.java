    protected String fetchRemoteFile(String host, String path) throws IOException {
        Reader r = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) ((new URL(("http://" + host + path)).openConnection()));
            if (socketTimeout > 0) {
                connection.setConnectTimeout(socketTimeout);
                connection.setReadTimeout(socketTimeout);
            }
            connection.addRequestProperty("User-Agent", userAgent + ' ' + version);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.connect();
            r = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringWriter sw = new StringWriter();
            int b;
            while ((b = r.read()) != -1) sw.write(b);
            return sw.toString();
        } finally {
            if (r != null) r.close();
        }
    }
