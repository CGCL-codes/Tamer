    public void checkForResourceExistence(String filename) {
        File file = new File(filename);
        if (file.exists()) return;
        URL url = myClassLoader.getResource(filename);
        if (url == null) return;
        InputStream in_stream = null;
        OutputStream out_stream = null;
        try {
            int b;
            in_stream = url.openStream();
            out_stream = new FileOutputStream(file);
            while ((b = in_stream.read()) != -1) out_stream.write(b);
        } catch (Exception ex) {
            XRepository.getLogger().warning(this, ex);
        } finally {
            try {
                in_stream.close();
                out_stream.close();
            } catch (Exception ex2) {
            }
        }
    }
