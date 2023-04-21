    CharBuffer getResourceAsCharBuffer() {
        URLConnection conn;
        InputStream input;
        try {
            input = url.openStream();
            conn = url.openConnection();
        } catch (IOException e2) {
            System.err.println(error = "getResourceAsCharBuffer:openConnection Failed " + url.getFile());
            return null;
        }
        int size = conn.getContentLength();
        if (size == -1) size = 10000;
        if (input != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            CharBuffer cbuf = CharBuffer.allocate(size);
            try {
                int len = reader.read(cbuf);
                reader.close();
                cbuf.limit(len);
                return cbuf;
            } catch (IOException e) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
                System.err.println(error = "getResourceAsCharBuffer:reader Failed" + url.getFile());
            }
        }
        return null;
    }
