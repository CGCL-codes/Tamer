    public InputStream sendXmlRpc(byte[] request) throws IOException {
        con = url.openConnection();
        System.err.println("Setting read timeout to 60000");
        con.setReadTimeout(timeout);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setAllowUserInteraction(false);
        con.setRequestProperty("Content-Length", Integer.toString(request.length));
        con.setRequestProperty("Content-Type", "text/xml");
        if (auth != null) {
            con.setRequestProperty("Authorization", "Basic " + auth);
        }
        OutputStream out = con.getOutputStream();
        out.write(request);
        out.flush();
        out.close();
        return con.getInputStream();
    }
