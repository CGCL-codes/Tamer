    public void play(URL url) {
        try {
            DataInputStream inputStream = new DataInputStream(url.openStream());
            play(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
