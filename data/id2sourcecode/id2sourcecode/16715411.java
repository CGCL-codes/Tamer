    public static void lookup(MojitoDHT dht, String[] args, PrintWriter out) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            KUID key = null;
            if (args[1].equals("kuid")) {
                key = KUID.createWithHexString(args[2]);
            } else {
                key = KUID.createWithBytes(md.digest(args[2].getBytes("UTF-8")));
            }
            md.reset();
            FindNodeResult evt = ((Context) dht).lookup(key).get();
            out.println(evt.toString());
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
