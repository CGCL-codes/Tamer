    public static void write(URL url, String str) throws IOException {
        URLConnection uc = url.openConnection();
        uc.setDoOutput(true);
        OutputStream os = uc.getOutputStream();
        try {
            os.write(str.getBytes(encoding));
            os.flush();
        } finally {
            os.close();
        }
    }
