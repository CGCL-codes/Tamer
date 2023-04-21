    public void loadFpg(URL url) {
        try {
            InputStream fin = url.openStream();
            in = new DataInputStream(new BufferedInputStream(fin));
            readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
