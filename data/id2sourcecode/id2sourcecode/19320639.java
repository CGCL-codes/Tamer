    public File getMidia(String url, String nomeFoto) throws Exception {
        File outputFile = new File("");
        try {
            HttpURLConnection c = (HttpURLConnection) (new URL(url)).openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            String PATH = Environment.getExternalStorageDirectory() + "/";
            File file = new File(PATH);
            outputFile = new File(file, nomeFoto);
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
            Log.d("FILTRO", "Error: " + e);
        }
        return outputFile.getAbsoluteFile();
    }
