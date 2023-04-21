    private static byte[] fetchURL(String url_string) {
        try {
            URL url = new URL(url_string);
            URLConnection connection = url.openConnection();
            byte[] bytes = new byte[connection.getContentLength()];
            int offset = 0;
            while (true) {
                int len = connection.getInputStream().read(bytes, offset, bytes.length - offset);
                if (len == -1) {
                    break;
                }
                offset += len;
            }
            return bytes;
        } catch (Exception exc) {
            return null;
        }
    }
