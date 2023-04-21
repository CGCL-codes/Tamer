    private static byte[] readGameFile(String file) {
        URL url = null;
        try {
            url = new URL(GAME_FILE_URL, file);
            return FileUtil.getBytes(url.openStream());
        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Bad URL: " + url);
            throw new IllegalArgumentException("Bad URL: " + url, e);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "File not found: " + url);
            throw new IllegalArgumentException("File not found: " + url, e);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not copy file: " + url);
            throw new IllegalArgumentException("Could not copy file: " + url, e);
        }
    }
