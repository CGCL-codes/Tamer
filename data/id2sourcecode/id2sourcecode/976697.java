    public static Object load(String strategy) {
        try {
            Class cl = Class.forName(strategy);
            boolean hasNoArgumentConstructor = false;
            Constructor[] constructors = cl.getConstructors();
            for (int i = 0; i < constructors.length; i++) {
                if (constructors[i].getParameterTypes().length == 0) {
                    hasNoArgumentConstructor = true;
                    break;
                }
            }
            if (!hasNoArgumentConstructor) return null;
            Object obj = cl.newInstance();
            return obj;
        } catch (Exception e) {
            return null;
        }
    }
