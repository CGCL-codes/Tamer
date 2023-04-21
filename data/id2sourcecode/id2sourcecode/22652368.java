    public static AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream inputStream = new BufferedInputStream(url.openStream());
        try {
            if (DEBUG == true) {
                System.err.println("Using AppletVorbisSPIWorkaround to get codec (AudioInputStream:url)");
            }
            return getAudioInputStream(inputStream);
        } catch (UnsupportedAudioFileException e) {
            inputStream.close();
            throw e;
        } catch (IOException e) {
            inputStream.close();
            throw e;
        }
    }
