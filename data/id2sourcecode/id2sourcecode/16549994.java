    public static String hash(File file, String algorithm, ProgressMeter progressMeter) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            fileInputStream = new FileInputStream(file);
            DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, digest);
            if (progressMeter != null) {
                progressMeter.setStartTime();
                progressMeter.setFinish(file.length());
            }
            byte[] buffer = new byte[8192];
            int bytesRead = 1;
            if (progressMeter != null) {
                while ((bytesRead = digestInputStream.read(buffer)) != -1) {
                    progressMeter.incrementCurrentPosition(bytesRead);
                }
            } else {
                while ((bytesRead = digestInputStream.read(buffer)) != -1) {
                }
            }
            byte[] hash = digest.digest();
            if (progressMeter != null) {
                progressMeter.setEndTime();
            }
            return new BigInteger(1, hash).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            StreamUtils.closeStream(fileInputStream);
        }
    }
