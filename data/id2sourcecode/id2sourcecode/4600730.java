    private static String FixString(String s) {
        if (ignoreCase) s = s.toUpperCase();
        char[] a = s.toCharArray();
        int len = a.length;
        if (len == 2) SemError(29);
        boolean spaces = false;
        int start = a[0];
        for (int i = 1; i <= len - 2; i++) {
            if (a[i] <= ' ') spaces = true;
            if (a[i] == '\\') {
                if (a[i + 1] == '\\' || a[i + 1] == '\'' || a[i + 1] == '\"') {
                    for (int j = i; j < len - 1; j++) a[j] = a[j + 1];
                    len--;
                }
            }
        }
        a[0] = '"';
        a[len - 1] = '"';
        if (spaces) SemError(24);
        return new String(a, 0, len);
    }
