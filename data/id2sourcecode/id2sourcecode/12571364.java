    public String fetchSite(URL url) throws IOException {
        InputStream in = url.openStream();
        StringBuffer sb = new StringBuffer();
        byte[] buffer = new byte[256];
        while (true) {
            int bytesRead = in.read(buffer);
            if (bytesRead == -1) break;
            for (int i = 0; i < bytesRead; i++) sb.append((char) buffer[i]);
        }
        return sb.toString();
    }
