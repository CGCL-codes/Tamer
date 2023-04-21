    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("127.0.0.1;Mozilla/5.0 (Windows NT 6.1) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.77 Safari/534.24 ChromePlus/1.6.2.0");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("127.0.0.1;Mozilla/5.0 (Windows NT 6.1) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.77 Safari/534.24 ChromePlus/1.6.2.0");
        final long st = System.currentTimeMillis();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] dg = md.digest(sb.toString().getBytes("utf-8"));
            byte[] dg2 = md.digest(sb2.toString().getBytes("utf-8"));
            final StringBuilder pm = new StringBuilder();
            pm.append("\n#######################################\n");
            pm.append("string: ").append(sb).append("\n");
            pm.append("string: ").append(sb2).append("\n");
            pm.append("md5: ").append(Arrays.toString(dg)).append("\n");
            pm.append("md5: ").append(Arrays.toString(dg2)).append("\n");
            pm.append("#######################################");
            final long et = System.currentTimeMillis();
            System.out.println(pm);
            System.out.println("Time: " + (et - st) / 1000.0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
