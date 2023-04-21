    public AreaFile(URL url) throws AreaFileException {
        imageSource = url.toString();
        try {
            af = new DataInputStream(new BufferedInputStream(url.openStream()));
        } catch (IOException e) {
            fileok = false;
            throw new AreaFileException("Error opening URL for AreaFile:" + e);
        }
        isRemote = url.getProtocol().equalsIgnoreCase("adde");
        fileok = true;
        position = 0;
        readMetaData();
    }
