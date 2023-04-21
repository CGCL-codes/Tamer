    private String generateId() {
        String strResult = "-1";
        try {
            String randomNum = new Integer(prng.nextInt()).toString();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] result = md5.digest(randomNum.getBytes());
            strResult = new String(encoder.encode(result));
            strResult = strResult.trim();
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Cannot load MD5 algorithm...");
        } catch (Exception e) {
            System.err.println("Cannot encode result id...");
        }
        return strResult;
    }
