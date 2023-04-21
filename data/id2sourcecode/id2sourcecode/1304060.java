    private String downloadJar(String urlString) {
        InputStream in = null;
        FileOutputStream fout = null;
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            in = urlConnection.getInputStream();
            File tmpJarFile = File.createTempFile("rdbassistant", ".jar");
            fout = new FileOutputStream(tmpJarFile);
            int r;
            while ((r = in.read()) != -1) {
                fout.write((char) r);
            }
            fout.close();
            in.close();
            return tmpJarFile.getAbsolutePath();
        } catch (IOException e) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e2) {
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e2) {
                }
            }
            return null;
        }
    }
