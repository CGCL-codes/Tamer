    public void testURLConnector() throws Exception {
        URLConnector connector = new URLConnector();
        Properties properties = new Properties();
        properties.setProperty("backend", "http://example.com/");
        connector.configure(properties);
        connector.connect();
        OutputStream out = connector.getOutputStream();
        out.close();
        InputStream in = connector.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int read;
        do {
            read = in.read(buff);
            if (read == -1) break;
            baos.write(buff, 0, read);
        } while (true);
        String s = new String(baos.toByteArray());
        assertTrue(s.indexOf("http://www.rfc-editor.org/rfc/rfc2606.txt") > 0);
        connector.close();
    }
