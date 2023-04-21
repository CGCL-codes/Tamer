    public void notestConnectFailures() throws Exception {
        final URLContentManager manager = createDefaultURLContentManager();
        ServerSocket socket = new ServerSocket(0);
        int port = socket.getLocalPort();
        socket.close();
        for (int i = 0; i < 2000; i += 1) {
            try {
                URLContent content = manager.getURLContent("http://127.0.0.1:" + port, null, null);
                InputStream stream = content.getInputStream();
                byte[] bytes = new byte[1024];
                int read;
                while ((read = stream.read(bytes)) != -1) {
                    System.out.write(bytes, 0, read);
                }
                stream.close();
            } catch (IOException expected) {
                assertEquals("Connection refused when connecting to " + "/127.0.0.1:" + port, expected.getMessage());
            }
        }
    }
