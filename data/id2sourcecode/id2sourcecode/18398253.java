    private static BundleCheck getInstance(String name, File file, boolean saveProperty) {
        if (file == null) {
            return new BundleCheck(name);
        } else {
            StandaloneMessageDigest checkDigest = null;
            try {
                FileInputStream checkFileStream = new FileInputStream(file);
                checkDigest = StandaloneMessageDigest.getInstance("SHA-1");
                int readCount;
                byte[] messageStreamBuff = new byte[DIGEST_STREAM_BUFFER_SIZE];
                do {
                    readCount = checkFileStream.read(messageStreamBuff);
                    if (readCount > 0) {
                        checkDigest.update(messageStreamBuff, 0, readCount);
                    }
                } while (readCount != -1);
                checkFileStream.close();
            } catch (Exception e) {
                throw new RuntimeException("BundleCheck.addProperty() caught: " + e);
            }
            BundleCheck bc = new BundleCheck(checkDigest.digest());
            if (saveProperty) {
                bc.addProperty(name);
            }
            return bc;
        }
    }
