    public static String generateMD5Sum(String filePath) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        String retVal = "";
        FileInputStream fis = new FileInputStream(filePath);
        int available = fis.available();
        byte[] reed = new byte[available];
        fis.read(reed);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] ba = md.digest(reed);
        String hexChar = null;
        if (ba != null) {
            for (int i = 0; i < ba.length; i++) {
                if (ba[i] > 0) {
                    hexChar = Integer.toHexString(ba[i]);
                } else if (ba[i] < 0) {
                    hexChar = Integer.toHexString(ba[i]).substring(6);
                } else hexChar = "00";
                retVal += lpad(hexChar, '0', 2);
            }
        }
        return retVal;
    }
