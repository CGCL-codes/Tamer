    public String getDigest(String in) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            return (null);
        }
        byte data[] = new byte[40];
        data = md.digest(in.getBytes());
        String output = "";
        for (int i = 0; i < 20; i++) if ((data[i] > -1) && (data[i] < 16)) output = output + '0' + Integer.toHexString(((int) data[i] & 0xff)); else output = output + Integer.toHexString(((int) data[i] & 0xff));
        return (output);
    }
