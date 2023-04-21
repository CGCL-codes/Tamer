    ByteBuffer getResourceAsByteBuffer() {
        if (url != null) {
            URLConnection conn;
            InputStream input;
            try {
                input = url.openStream();
                conn = url.openConnection();
            } catch (IOException e2) {
                System.err.println(error = "getResourceAsByteArray:openConnection Failed " + url.getFile());
                return null;
            }
            int size = conn.getContentLength();
            int len;
            if (input != null) {
                byte bytes[] = new byte[size];
                try {
                    len = input.read(bytes);
                    input.close();
                    if (len != size) {
                        System.err.println(error = "getResourceAsByteArray:Short read " + url.getFile() + " Expected " + size + ">" + len);
                    }
                    return ByteBuffer.wrap(bytes);
                } catch (IOException e) {
                    try {
                        input.close();
                    } catch (IOException e1) {
                    }
                    System.err.println(error = "getResourceAsByteArray:Failed reading " + url.getFile());
                }
            }
        }
        return null;
    }
