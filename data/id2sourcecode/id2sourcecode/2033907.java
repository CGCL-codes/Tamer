    public String computeMD5(String toCode) {
        MessageDigest md;
        byte rr[];
        String result = "";
        try {
            md = MessageDigest.getInstance("MD5");
            rr = md.digest(toCode.getBytes("US-ASCII"));
            String tempS = "";
            int j = 0;
            for (int i = 0; i < 16; i++) {
                tempS = String.format("%x", rr[i]);
                j = tempS.length();
                if (j == 2) {
                    result = result.concat(tempS);
                } else if (j == 1) {
                    result = result.concat("0" + tempS);
                } else if (j > 2) {
                    result = result.concat(tempS.substring(14));
                }
            }
        } catch (Exception e) {
            System.out.println("excption in method computeMD5  " + e.getMessage());
        }
        return result;
    }
