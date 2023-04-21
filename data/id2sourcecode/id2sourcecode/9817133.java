    public static String getResourceFileReferenceWithSHA256HashAsHexEncodedString(File file) {
        String extension = ResourceFileSupport.getExtensionWithDotOrEmptyString(file.getName(), true);
        long size = file.length();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            FileInputStream inputStream = new FileInputStream(file);
            messageDigest.update(getExtraBytes(size, extension));
            try {
                byte[] buffer = new byte[4096];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    messageDigest.update(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            byte[] hashSHA256 = messageDigest.digest();
            String resourceFileReference = ResourceFileSupport.ResourceFilePrefix + hexEncodedStringForByteArray(hashSHA256) + getExtraString(size, extension);
            return resourceFileReference;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
