    public static String getContentResult(URL url) throws IOException {
        InputStream in = url.openStream();
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[256];
        while (true) {
            int byteRead = in.read(buffer);
            if (byteRead == -1) {
                break;
            }
            for (int i = 0; i < byteRead; i++) {
                sb.append((char) buffer[i]);
            }
        }
        return sb.toString();
    }
