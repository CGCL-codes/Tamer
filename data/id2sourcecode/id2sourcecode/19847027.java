    public static String sendBytes(byte[] data, String urlServer, String rt) throws UnsupportedEncodingException, MalformedURLException, IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending bytes to url: " + urlServer);
        }
        URL url = new URL(urlServer);
        URLConnection conn = url.openConnection();
        conn.setReadTimeout(0);
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setRequestProperty("Content-Transfer-Encoding", "binary");
        conn.setRequestProperty("Content-Length", new Integer(data.length).toString());
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.connect();
        if (LOGGER.isTraceEnabled()) LOGGER.trace("must send " + data.length + " bytes ");
        OutputStream out = conn.getOutputStream();
        out.write(data);
        out.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder readContent = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            readContent.append(line);
            readContent.append(rt);
        }
        out.close();
        rd.close();
        return readContent.toString();
    }
