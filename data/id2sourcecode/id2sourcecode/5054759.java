    private boolean readFile(SampleResult result) {
        File file = new File(getFileName());
        if (!file.exists()) {
            String msg = "File " + getFileName() + " does not exist, error checking file.";
            log.info(msg);
            result.setResponseMessage(msg);
            return false;
        }
        InputStream fis = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            if (isBufferedIo()) {
                Debug.debug("Buffered IO", 3);
                fis = new BufferedInputStream(new FileInputStream(file));
            } else {
                Debug.debug("NON Buffered IO", 3);
                fis = new FileInputStream(file);
            }
            int i;
            while ((i = fis.read()) != -1) {
                md5.update((byte) i);
            }
            if (!MessageDigest.isEqual(md5Digest, md5.digest())) {
                result.setResponseMessage("MD5 digest not identical");
                return false;
            }
            result.setResponseMessage(new String(md5Digest));
        } catch (IOException e) {
            log.error("Unable to read file", e);
            result.setResponseMessage(e.getMessage());
            return false;
        } catch (NoSuchAlgorithmException e1) {
            log.error("Error creating the digest", e1);
            result.setResponseMessage(e1.getMessage());
            return false;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
            }
        }
        return true;
    }
