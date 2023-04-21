    public AreaFile(String filename, Applet parent) throws AreaFileException {
        URL url;
        imageSource = filename;
        try {
            url = new URL(parent.getDocumentBase(), filename);
        } catch (MalformedURLException e) {
            fileok = false;
            throw new AreaFileException("Error opening URL for AreaFile:" + e);
        }
        try {
            af = new DataInputStream(new BufferedInputStream(url.openStream()));
        } catch (IOException e) {
            fileok = false;
            throw new AreaFileException("Error opening AreaFile:" + e);
        }
        isRemote = url.getProtocol().equalsIgnoreCase("adde");
        fileok = true;
        position = 0;
        readMetaData();
    }
