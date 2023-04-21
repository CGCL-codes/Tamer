    private void ensureDataSourceExists() throws IOException {
        if (!dataSourceExists()) {
            String path = "mkdirServlet?dir=data/" + dataSource;
            HttpURLConnection con = makeGetConnection(path);
            if (con.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new IOException("couldn't create " + dataSource + " got " + con.getResponseCode());
            }
            con.getInputStream().close();
            if (!dataSourceExists()) {
                throw new IOException("newly created folder " + dataSource + " isn't recognized by server.  perhaps caching is on." + "  see https://vessel.bbn.com/trac/wiki/SettingUpAuthorServlet" + " for configuration instructions");
            }
            String readmePath = "data/" + dataSource + "/vessel-readme.txt";
            HttpURLConnection con2 = makePutConnection(readmePath);
            OutputStream outputStream = con2.getOutputStream();
            outputStream.close();
            if ((con2.getResponseCode() != HttpURLConnection.HTTP_CREATED) && (con2.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT)) {
                throw new IOException(con2.getResponseCode() + ": couldn't write readme file to " + dataSource);
            }
            con2.getInputStream().close();
        }
    }
