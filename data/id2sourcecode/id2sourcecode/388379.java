    public static void main(String[] args) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No such algorithm.!");
            return;
        }
        String in = "Test";
        if (args.length > 0) in = args[0];
        byte data[] = new byte[40];
        data = md.digest(in.getBytes());
        String output = "";
        for (int i = 0; i < 20; i++) if ((data[i] > -1) && (data[i] < 16)) output = output + '0' + Integer.toHexString(((int) data[i] & 0xff)); else output = output + Integer.toHexString(((int) data[i] & 0xff));
        System.out.println(output);
    }
