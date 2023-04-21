    public String transform(String s) {
        try {
            byte[] hash = MessageDigest.getInstance("md5").digest(s.getBytes("UTF-8"));
            StringBuffer hashString = new StringBuffer();
            for (byte aHash : hash) {
                String hex = Integer.toHexString(aHash);
                if (hex.length() == 1) {
                    hashString.append("0");
                    hashString.append(hex.charAt(hex.length() - 1));
                } else {
                    hashString.append(hex.substring(hex.length() - 2));
                }
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
