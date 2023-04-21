    public static String writeToFile(DataHandler dataHandler, String fileName, String targetPath) throws SkceWSException {
        String fileFullPath = "";
        InputStream inputStream = null;
        File outputFile = null;
        FileOutputStream fos = null;
        logger.info("Util.writeToFile: fileName = " + fileName);
        logger.info("Util.writeToFile: targetPath = " + targetPath);
        try {
            if (dataHandler != null) {
                inputStream = dataHandler.getInputStream();
                if (inputStream != null) {
                    if (targetPath != null) {
                        outputFile = new File(targetPath + CommonWS.fs + fileName);
                        fos = new FileOutputStream(outputFile);
                        logger.info("Util.writeToFile: writing to target..");
                        byte[] b = new byte[1024];
                        int numread;
                        while ((numread = inputStream.read(b)) != -1) {
                            fos.write(b, 0, numread);
                            fos.flush();
                        }
                        logger.info("Util.writeToFile: wrote to target.");
                        fos.close();
                        inputStream.close();
                        fileFullPath = outputFile.getAbsolutePath();
                    } else {
                        logger.severe("targetPath is null. Please specify the target location to write the file");
                        throw new SkceWSException("Please specify the target location to write the file");
                    }
                } else {
                    logger.severe("No data in datahandler");
                    throw new SkceWSException("Datahandler has no data");
                }
            } else {
                logger.severe("Datahandler is null");
                throw new SkceWSException("No DataHandler specified");
            }
        } catch (FileNotFoundException e) {
            logger.severe(e.getLocalizedMessage());
            throw new SkceWSException(e);
        } catch (IOException e) {
            logger.severe(e.getLocalizedMessage());
            throw new SkceWSException(e);
        } finally {
            logger.info("Util.writeToFile: closing the streams..");
            try {
                if (inputStream != null) inputStream.close();
                if (fos != null) fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        logger.fine("Util.writeToFile: returning the file path..");
        return fileFullPath;
    }
