    public final String write(final byte[] content) throws DSSException {
        logger.debug("write(...)");
        if (content.length == 0) {
            String errMsg = "Cannot write an empty block";
            logger.error(errMsg);
            throw new DSSException(errMsg);
        } else {
            byte[] hashcode = null;
            String strHashcode = "";
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(content);
                hashcode = md.digest();
            } catch (NoSuchAlgorithmException e) {
                String errMsg = "Could not calculate hashcode: " + e.getMessage();
                logger.error(errMsg);
                throw new DSSException(errMsg);
            }
            strHashcode = Base64.encode(hashcode);
            String name = strHashcode.replace('/', '_');
            logger.debug("New block name: " + name);
            if (exists(name)) {
                logger.debug("block already exists");
                return name;
            } else {
                logger.debug("storing block");
                String absoluteBlockPath = storeDirectory + File.separator + name;
                File f = new File(absoluteBlockPath);
                FileOutputStream fos = null;
                try {
                    if (f.createNewFile()) {
                        fos = new FileOutputStream(f);
                        fos.write(content);
                    } else {
                        String errMsg = "Could not create a new file when attempting to write block.";
                        logger.error(errMsg);
                        throw new DSSException(errMsg);
                    }
                } catch (FileNotFoundException e) {
                    String errMsg = "Could not open FileOutputStream to write block: " + e.getMessage();
                    logger.error(errMsg);
                    throw new DSSException(errMsg);
                } catch (IOException e) {
                    String errMsg = "Could not write block: " + e.getMessage();
                    logger.error(errMsg);
                    throw new DSSException(errMsg);
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            logger.error("Could not close FileOutputStream: " + e.getMessage());
                        }
                    }
                }
                return name;
            }
        }
    }
