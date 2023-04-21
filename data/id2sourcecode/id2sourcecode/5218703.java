    private String getNewestJavaSubdirectory(String baseURL) {
        try {
            URL url = new URL(baseURL + ";a=tree");
            InputStream input = url.openStream();
            int off = 0, len = 16384;
            byte[] buffer = new byte[len];
            for (; ; ) {
                int count = input.read(buffer, off, len);
                if (count < 0) break;
                off += count;
                len -= count;
            }
            final String content = new String(buffer, 0, off);
            final String key = ";a=tree;f=";
            off = content.indexOf(key);
            if (off >= 0) {
                off += key.length();
                int end = content.indexOf(';', off);
                return content.substring(off, end);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
