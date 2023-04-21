    @Override
    public String put(String fileName, String metadata, long sid, long did) throws ArUnvalidIndexException, ArFileException, ArFileWormException {
        if (this.isHold()) {
            throw new ArUnvalidIndexException("Legacy is not ready");
        }
        ArkFsDocAbstract doc = (ArkFsDocAbstract) getDocument(sid, did);
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
        if (isNetwork) {
            doc.write(inputStream);
        } else {
            fileChannelIn = fileInputStream.getChannel();
            doc.write(fileChannelIn);
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
        String dkey = doc.getDKey();
        doc.clear();
        doc = null;
        return dkey;
    }
