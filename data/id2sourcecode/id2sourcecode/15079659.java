    private static String readFile(String sUrl) throws IOException {
        StringBuffer s = new StringBuffer();
        try {
            URL url = new URL(sUrl);
            InputStream f = url.openStream();
            int len = f.available();
            for (int i = 1; i <= len; i++) {
                s.append((char) f.read());
            }
            f.close();
        } catch (IOException e) {
            if (e.toString().indexOf("code: 400") == -1) throw e; else if (e.toString().indexOf("code: 500") == -1) {
                try {
                    Thread.sleep(1000 * 60 * 5);
                } catch (Exception e2) {
                }
                throw e;
            } else System.out.println(e);
        }
        return s.toString();
    }
