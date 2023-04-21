    public static void updateCSSClass(Object el, String cssClazz, boolean kept) {
        Element e = (Element) el;
        String className = e.className;
        if (className == null || className.length() == 0) {
            if (kept) {
                e.className = cssClazz;
            }
            return;
        }
        String[] clazz = className.split("\\s");
        for (int i = 0; i < clazz.length; i++) {
            if (clazz[i] == cssClazz) {
                if (kept) {
                    return;
                }
                for (int j = i; j < clazz.length - 1; j++) {
                    clazz[j] = clazz[j + 1];
                }
                {
                }
                return;
            }
        }
        if (kept) {
            clazz[clazz.length] = cssClazz;
            {
            }
        }
    }
