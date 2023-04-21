    public AreaDirectoryList(URL url) throws AreaFileException {
        try {
            inputStream = new DataInputStream(new BufferedInputStream(url.openStream()));
        } catch (IOException e) {
            throw new AreaFileException("Error opening URL for AreaFile:" + e);
        }
        readDirectory();
    }
