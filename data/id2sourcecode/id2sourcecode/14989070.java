    public void loadFromFile(URL url) {
        try {
            InputStream input = url.openStream();
            loadFromFile(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
