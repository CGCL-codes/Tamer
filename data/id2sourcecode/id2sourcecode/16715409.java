    public static void remove(MojitoDHT dht, String[] args, final PrintWriter out) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            KUID key = null;
            if (args[1].equals("kuid")) {
                key = KUID.createWithHexString(args[2]);
            } else {
                key = KUID.createWithBytes(md.digest(args[2].getBytes("UTF-8")));
            }
            md.reset();
            out.println("Removing... " + key);
            StoreResult evt = dht.remove(key).get();
            StringBuilder buffer = new StringBuilder();
            buffer.append("REMOVE RESULT:\n");
            buffer.append(evt.toString());
            out.println(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
