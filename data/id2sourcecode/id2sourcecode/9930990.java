    private void uploadIndexFile(FrostIndex idx) throws Throwable {
        loadIndex(currentDate);
        File indexFile = new File(keypool + board.getBoardFilename() + "_upload.zip");
        XMLTools.writeXmlFile(XMLTools.getXMLDocument(idx), indexFile.getPath());
        boolean success = false;
        int tries = 0;
        String[] result = { "Error", "Error" };
        if (indexFile.length() > 0 && indexFile.isFile()) {
            boolean signUpload = MainFrame.frostSettings.getBoolValue("signUploads");
            byte[] metadata = null;
            FileAccess.writeZipFile(FileAccess.readByteArray(indexFile), "entry", indexFile);
            if (signUpload) {
                byte[] zipped = FileAccess.readByteArray(indexFile);
                SignMetaData md = new SignMetaData(zipped, identities.getMyId());
                metadata = XMLTools.getRawXMLDocument(md);
            }
            int index = findFreeUploadIndex();
            while (!success && tries <= MAX_TRIES) {
                result = FcpInsert.putFile(insertKey + index + ".idx.sha3.zip", indexFile, metadata, insertHtl, false);
                if (result[0].equals("Success")) {
                    success = true;
                    setIndexSuccessfull(index);
                    logger.info("FILEDN:***** Index file successfully uploaded *****");
                } else {
                    if (result[0].equals("KeyCollision")) {
                        index = findFreeUploadIndex(index);
                        tries = 0;
                        logger.info("FILEDN:***** Index file collided, increasing index. *****");
                    } else {
                        String tv = result[0];
                        if (tv == null) tv = "";
                        logger.info("FILEDN:***** Unknown upload error (#" + tries + ", '" + tv + "'), retrying. *****");
                    }
                }
                tries++;
            }
        }
    }
