    public static boolean download(String url, String dest) throws IOException {
        try {
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(new java.net.URL(url).openStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(dest);
            int oneChar = 0;
            while ((oneChar = in.read()) >= 0) {
                fos.write(oneChar);
            }
            in.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
