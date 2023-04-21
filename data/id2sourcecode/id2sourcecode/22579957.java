    public static void assertBodyEqualsURLBody(HttpMethod method, String url) {
        try {
            URLConnection conn = new URL(url).openConnection();
            byte[] data;
            int len = conn.getContentLength();
            if (len > 0) {
                data = new byte[len];
                int read = 0;
                InputStream in = conn.getInputStream();
                while (read < len) {
                    read += in.read(data, read, len - read);
                }
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream stream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                while (stream.available() > 0) {
                    int read = stream.read(buffer);
                    baos.write(buffer, 0, read);
                }
                data = baos.toByteArray();
            }
            assertBodyEqualsData(method, data);
        } catch (Exception e) {
            throw new RuntimeException("Unable to open reference data form url : " + url, e);
        }
    }
