    public static void get(MojitoDHT dht, String[] args, PrintWriter out) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            KUID key = null;
            if (args[1].equals("kuid")) {
                key = KUID.createWithHexString(args[2]);
            } else {
                key = KUID.createWithBytes(md.digest(args[2].getBytes("UTF-8")));
            }
            md.reset();
            EntityKey lookupKey = EntityKey.createEntityKey(key, DHTValueType.ANY);
            FindValueResult evt = dht.get(lookupKey).get();
            out.println(evt.toString());
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
