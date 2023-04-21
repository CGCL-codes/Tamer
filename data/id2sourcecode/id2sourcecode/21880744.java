    public void saveProject(URL url, TaskModelImpl model) throws TransformerException, ParserConfigurationException, IOException {
        if ("file".equals(url.getProtocol())) {
            File f;
            try {
                f = new File(url.toURI());
            } catch (URISyntaxException e) {
                f = new File(url.getPath());
            }
            saveProject(f, model);
        } else if ("http".equals(url.getProtocol())) {
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            saveProject(tmp, model);
            OutputStream out = null;
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Length", String.valueOf(tmp.size()));
                con.setRequestProperty("Content-Type", "application/xml");
                con.connect();
                out = con.getOutputStream();
                out.write(tmp.toByteArray());
                int responseCode = con.getResponseCode();
                if (responseCode != 200) {
                    throw new IOException("HTTP Error " + responseCode + ": " + con.getResponseMessage());
                }
            } finally {
                if (out != null) {
                    out.close();
                }
                if (con != null) {
                    con.disconnect();
                }
            }
        } else {
            throw new IOException("Unsupported url protocol: " + url.getProtocol());
        }
    }
