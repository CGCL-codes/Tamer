    public static void put(MojitoDHT dht, String[] args, final PrintWriter out) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            KUID key = null;
            byte[] value = null;
            if (args[1].equals("kuid")) {
                key = KUID.createWithHexString(args[2]);
            } else {
                key = KUID.createWithBytes(md.digest(args[2].getBytes("UTF-8")));
            }
            md.reset();
            if (args[3].equals("value")) {
                value = args[4].getBytes("UTF-8");
            } else if (args[3].equals("file")) {
                File file = new File(args[4]);
                byte[] data = new byte[(int) Math.min(1024, file.length())];
                FileInputStream in = new FileInputStream(file);
                in.read(data, 0, data.length);
                in.close();
                value = data;
            }
            md.reset();
            out.println("Storing... " + key);
            StoreResult evt = dht.put(key, new DHTValueImpl(DHTValueType.TEST, Version.ZERO, value)).get();
            StringBuilder buffer = new StringBuilder();
            buffer.append("STORE RESULT:\n");
            buffer.append(evt.toString());
            out.println(buffer.toString());
        } catch (Exception err) {
            err.printStackTrace(out);
        }
    }
