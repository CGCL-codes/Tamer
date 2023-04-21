    public ByteArrayOutputStream response() throws Exception {
        ByteArrayOutputStream resp = new ByteArrayOutputStream(1024);
        byte[] buf = new byte[1024 * 8];
        int hasread = 0;
        doVerbose(2, "IppClient.java: response(): Read from server '" + server.toString() + "' to ByteArrayOutputStream");
        BufferedInputStream s = new BufferedInputStream(server.getInputStream());
        while ((hasread = s.read(buf, 0, buf.length)) > 0) {
            doVerbose(2, "IppClient.java: response(): Read  " + hasread + " bytes from BufferedInputStream");
            resp.write(buf, 0, hasread);
            doVerbose(2, "IppClient.java: response(): Write " + hasread + " bytes to ByteArrayOutputStream");
        }
        s.close();
        doVerbose(2, "IppClient.java: response(): Close  BufferedInputStream");
        return resp;
    }
