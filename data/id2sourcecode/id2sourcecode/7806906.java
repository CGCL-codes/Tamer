    private File createFile(File p_storedFile, InputStream p_is, boolean overwrite) {
        OutputStream fileStream = null;
        try {
            logger.debug("Filename to create=" + p_storedFile.getAbsolutePath());
            if (!p_storedFile.createNewFile() && !overwrite) {
                logger.info("Unable to create file " + p_storedFile.toString());
                return null;
            }
            fileStream = new FileOutputStream(p_storedFile, false);
            byte[] buff = new byte[2048];
            int read;
            while ((read = p_is.read(buff)) > 0) {
                fileStream.write(buff, 0, read);
            }
            fileStream.flush();
            return p_storedFile;
        } catch (FileNotFoundException e) {
            logger.debug(e);
            deleteFile(p_storedFile);
        } catch (IOException e) {
            logger.debug(e);
            deleteFile(p_storedFile);
        } finally {
            try {
                if (fileStream != null) fileStream.close();
            } catch (IOException e) {
                logger.debug(e);
            }
        }
        return null;
    }
