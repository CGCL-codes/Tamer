    public static void checkPermission(String p) {
        if (p.equals("none") || p.equals("read") || p.equals("write") || p.equals("delete")) return;
        throw new IllegalArgumentException("not a permission : " + p);
    }
