    public AreaDirectoryList(String imageSource) throws AreaFileException {
        try {
            inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(imageSource), 2048));
        } catch (IOException eIO) {
            URL url;
            try {
                url = new URL(imageSource);
                urlc = url.openConnection();
                InputStream is = urlc.getInputStream();
                inputStream = new DataInputStream(new BufferedInputStream(is));
            } catch (Exception e) {
                throw new AreaFileException("Error opening AreaFile: " + e);
            }
            if (url.getProtocol().equalsIgnoreCase("adde")) isADDE = true;
        }
        readDirectory();
    }
