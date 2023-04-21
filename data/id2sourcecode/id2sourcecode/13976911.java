    public static void main(String[] a) {
        try {
            byte[] msg1 = "MessageDigest.getInstance(\"GOOGOO\");".getBytes("ASCII");
            byte[] aab = "aab".getBytes("ASCII");
            byte[] cdd = "cdd".getBytes("ASCII");
            byte[] aa = "aa".getBytes("ASCII");
            byte[] bc = "bc".getBytes("ASCII");
            byte[] dd = "dd".getBytes("ASCII");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg1);
            md.update(aab);
            md.update(cdd);
            println("one: ", md.digest());
            md.update(msg1);
            md.update(aa);
            md.update(dd);
            md.update(bc);
            println("two: ", md.digest());
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }
