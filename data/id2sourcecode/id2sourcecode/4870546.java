    public static boolean copy(Object src, Object dst) {
        if (src == null || dst == null) return false;
        try {
            Stream s0 = new Stream(src);
            Stream s1 = new Stream(dst);
            try {
                s1.write(s0.read());
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
        }
        if ((dst instanceof Collection) || dst.getClass().isArray()) {
            if ((src instanceof Collection) || src.getClass().isArray()) {
                Object[] a1 = toArray(src);
                Object[] a2 = toArray(dst);
                System.arraycopy(a1, 0, a2, 0, (a1.length < a2.length) ? a1.length : a2.length);
                return true;
            }
        }
        Map m0 = toMap(src);
        Map m1 = toMap(dst);
        Iterator iter = m0.entrySet().iterator();
        int copied = 0;
        while (iter.hasNext()) {
            try {
                Map.Entry entry = (Map.Entry) iter.next();
                m1.put(entry.getKey(), entry.getValue());
                copied++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return copied > 0;
    }
