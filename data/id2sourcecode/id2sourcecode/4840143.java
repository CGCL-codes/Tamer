    public static boolean removeCSSClass(Object el, String cssClazz) {
        Element e = (Element) el;
        String className = e.className;
        if (className == null || className.length() == 0) {
            return false;
        }
        String[] clazz = className.split("\\s");
        boolean existed = false;
        for (int i = 0; i < clazz.length; i++) {
            if (clazz[i] == cssClazz) {
                existed = true;
                for (int j = i; j < clazz.length - 1; j++) {
                    clazz[j] = clazz[j + 1];
                }
                {
                }
                break;
            }
        }
        if (existed) {
        }
        return existed;
    }
