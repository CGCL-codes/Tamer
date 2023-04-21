    private int getOnePoint(String strType, Hashtable fenshu) {
        int[] t01 = (int[]) fenshu.get(strType);
        int t001 = t01[0];
        int[] t02 = new int[t01.length - 1];
        for (int i = 0; i < t02.length; i++) {
            t02[i] = t01[i + 1];
        }
        fenshu.put(strType, t02);
        return t001;
    }
