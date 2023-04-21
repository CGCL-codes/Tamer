    public String getMD5(File file) throws IOException {
        BufferedInputStream inputStream = null;
        digest.reset();
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            for (int numberOfBytesRead = inputStream.read(buffer); numberOfBytesRead != -1; numberOfBytesRead = inputStream.read(buffer)) {
                digest.update(buffer);
            }
            return convertByteArrayToString(digest.digest());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
