    private String getSessionId(String name, String mdp) {
        Calendar c = Calendar.getInstance();
        @SuppressWarnings("static-access") String date = c.get(c.DAY_OF_MONTH) + "/" + c.get(c.MONTH) + "/" + c.get(c.YEAR) + " " + c.getTime();
        String str = name + ":" + mdp + ":" + date;
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(str.getBytes());
            StringBuilder hashString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(hash[i]);
                if (hex.length() == 1) {
                    hashString.append('0');
                    hashString.append(hex.charAt(hex.length() - 1));
                } else hashString.append(hex.substring(hex.length() - 2));
            }
            return hashString.toString();
        } catch (Exception ex) {
        }
        return "123";
    }
