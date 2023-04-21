    public String generatedMD5String(String pwd) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("SHA");
            byte[] arr = messagedigest.digest(pwd.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < arr.length; i++) {
                sb.append(Integer.toHexString(arr[i] & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
