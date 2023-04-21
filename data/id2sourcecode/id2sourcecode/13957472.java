    public static String readFileAll(InputStream res) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int read = 0;
            while ((read = res.read(buf)) != -1) {
                baos.write(buf, 0, read);
            }
            res.close();
            return baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Missing";
    }
