    static String getFingerPrint(HASH hash, byte[] data) {
        try {
            hash.init();
            hash.update(data, 0, data.length);
            byte[] foo = hash.digest();
            StringBuffer sb = new StringBuffer();
            int bar;
            for (int i = 0; i < foo.length; i++) {
                bar = foo[i] & 0xff;
                sb.append(chars[(bar >>> 4) & 0xf]);
                sb.append(chars[(bar) & 0xf]);
                if (i + 1 < foo.length) sb.append(":");
            }
            return sb.toString();
        } catch (Exception e) {
            return "???";
        }
    }
