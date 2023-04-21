    protected void processStreamRequest() {
        try {
            java.net.URL url = (java.net.URL) this.socket.readObject();
            String fileName = url.getFile();
            url = ClassLoader.getSystemResource(fileName);
            InputStream stream;
            if (url == null) {
                stream = new java.io.ByteArrayInputStream(new byte[0]);
            } else {
                stream = url.openStream();
            }
            this.socket.writeFromStream(stream);
        } catch (Exception e) {
            System.out.println("SrvClassRequest>>Error processing stream request " + e);
            e.printStackTrace();
        }
    }
