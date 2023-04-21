    public static void toggleCSSClass(Object el, String cssClazz) {
        Element e = (Element) el;
        String className = e.className;
        if (className == null || className.length() == 0) {
            e.className = cssClazz;
            return;
        }
        String[] clazz = className.split("\\s");
        for (int i = 0; i < clazz.length; i++) {
            if (clazz[i] == cssClazz) {
                for (int j = i; j < clazz.length - 1; j++) {
                    clazz[j] = clazz[j + 1];
                }
                {
                }
                return;
            }
        }
        clazz[clazz.length] = cssClazz;
        {
        }
    }
