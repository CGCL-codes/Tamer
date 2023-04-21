    private boolean writeFile(SampleResult result) {
        File file = new File(getFileName());
        if (file.exists()) {
            log.info("Deleting file " + getFileName() + " since it allready exists.");
            file.delete();
        }
        Random random = new Random(System.currentTimeMillis());
        try {
            MessageDigest md5 = null;
            if (isCheckMd5()) {
                md5 = MessageDigest.getInstance("MD5");
            }
            OutputStream fos;
            if (isBufferedIo()) {
                Debug.debug("Buffered IO", 3);
                fos = new BufferedOutputStream(new FileOutputStream(file));
            } else {
                Debug.debug("NON Buffered IO", 3);
                fos = new FileOutputStream(file);
            }
            long numberOfBytes = numberOfKBytes * 1024;
            for (long i = 0; i < numberOfBytes; i++) {
                int b = random.nextInt();
                fos.write(b);
                if (md5 != null) {
                    md5.update((byte) b);
                }
            }
            fos.close();
            fos.flush();
            if (md5 != null) {
                md5Digest = md5.digest();
            }
            result.setBytes((int) numberOfBytes);
        } catch (IOException e) {
            log.error("Unable to write file", e);
            result.setResponseMessage(e.getMessage());
            return false;
        } catch (NoSuchAlgorithmException e1) {
            log.error("Error creating the digest", e1);
            result.setResponseMessage(e1.getMessage());
            return false;
        }
        return true;
    }
