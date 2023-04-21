    public static String XgetSHA1HashAsHexEncodedString(File file) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            FileInputStream inputStream = new FileInputStream(file);
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
            byte[] hashSHA1 = messageDigest.digest();
            return hexEncodedStringForByteArray(hashSHA1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
