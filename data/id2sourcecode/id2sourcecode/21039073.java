    public long put(String fileName, long sid, long did) throws ArUnvalidIndexException, ArFileException {
        if (this.is_stopped) {
            throw new ArUnvalidIndexException("Legacy is not started");
        }
        ArDocInterface doc = getDocument(sid, did);
        FileChannel fileChannelIn = null;
        FileInputStream fileInputStream = null;
        InputStream inputStream = null;
        boolean isNetwork = true;
        try {
            URI uri = new URI(fileName);
            if (uri.getScheme().equalsIgnoreCase("file")) {
                isNetwork = false;
            }
            if (isNetwork) {
                URL url = uri.toURL();
                inputStream = url.openStream();
                url = null;
            } else {
                fileInputStream = new FileInputStream(fileName);
            }
            uri = null;
        } catch (Exception e) {
            try {
                fileInputStream = new FileInputStream(fileName);
            } catch (FileNotFoundException e1) {
                throw new ArFileException("Error during open InputStream in put:" + fileName);
            }
            isNetwork = false;
        }
        long size = 0;
        if (isNetwork) {
            size = doc.write(inputStream);
        } else {
            fileChannelIn = fileInputStream.getChannel();
            size = doc.write(fileChannelIn);
        }
        try {
            if (isNetwork) {
                inputStream.close();
            } else {
                fileChannelIn.close();
            }
        } catch (IOException e) {
            logger.info("Error during close while put", e);
        }
        fileChannelIn = null;
        return size;
    }
