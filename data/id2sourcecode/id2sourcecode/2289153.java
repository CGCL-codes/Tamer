    public static void saveURL(String address, File outputFile) throws IOException {
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(false);
        InputStream is = conn.getInputStream();
        FileOutputStream fos = new FileOutputStream(outputFile);
        byte[] chunk = new byte[128];
        int read = is.read(chunk);
        while (read != -1) {
            fos.write(chunk, 0, read);
            read = is.read(chunk);
        }
        is.close();
        fos.close();
    }
