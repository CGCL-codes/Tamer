    private ImmutableMarkup makeRequest(final String URIArguments) throws QwicapException, IOException {
        final URL ReqURL = new URL(GameURLStr + URIArguments);
        final HttpURLConnection Conn = (HttpURLConnection) ReqURL.openConnection();
        Cookies.writeTo(Conn);
        Conn.setDefaultUseCaches(false);
        Conn.setUseCaches(false);
        Conn.setRequestProperty("Connection", "close");
        Conn.setRequestMethod("GET");
        Conn.setDoOutput(false);
        Conn.setDoInput(true);
        Conn.setConnectTimeout(0);
        Conn.setReadTimeout(0);
        Conn.connect();
        final InputStream ConnIn = Conn.getInputStream();
        try {
            Cookies.readFrom(Conn);
            BOut.reset();
            for (int BytesRead = ConnIn.read(ReadBuff); BytesRead >= 0; BytesRead = ConnIn.read(ReadBuff)) BOut.write(ReadBuff, 0, BytesRead);
        } finally {
            try {
                ConnIn.close();
            } catch (Exception e) {
                Log.log(Level.WARNING, "ConnIn.close() failed.", e);
            }
            try {
                Conn.disconnect();
            } catch (Exception e) {
                Log.log(Level.WARNING, "Conn.disconnect() failed.", e);
            }
        }
        return new XMLDocument(BOut.getInputStream(), BOut.size());
    }
