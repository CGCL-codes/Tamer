    public static long getObjectSize(Class aClass) {
        long result = 0;
        try {
            aClass.getConstructor(new Class[] {});
        } catch (NoSuchMethodException ex) {
            System.err.println(aClass + " does not have a no-argument constructor.");
            return result;
        }
        Object[] objects = new Object[fSAMPLE_SIZE];
        try {
            @SuppressWarnings("unused") Object throwAway = aClass.newInstance();
            long startMemoryUse = getMemoryUse();
            for (int idx = 0; idx < objects.length; ++idx) {
                objects[idx] = aClass.newInstance();
            }
            long endMemoryUse = getMemoryUse();
            float approximateSize = (endMemoryUse - startMemoryUse) / 100f;
            result = Math.round(approximateSize);
        } catch (Exception ex) {
            System.err.println("Cannot create object using " + aClass);
        }
        return result;
    }
