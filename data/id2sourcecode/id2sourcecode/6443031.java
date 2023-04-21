    public static boolean downloadfileRosemont(String url1, String fileName, boolean isGet) {
        try {
            URL url = new URL(url1);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod(isGet ? "GET" : "POST");
            c.setDoOutput(true);
            c.connect();
            String PATH = Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator + "rosemont" + File.separator;
            File file = new File(PATH);
            File outputFile = new File(file, fileName);
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
