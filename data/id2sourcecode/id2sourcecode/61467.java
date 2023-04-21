    public static boolean method391(char ac[]) {
        boolean flag = true;
        for (int i = 0; i < ac.length; i++) if (!method388(ac[i]) && ac[i] != 0) flag = false;
        if (flag) return true;
        int j = method392(ac);
        int k = 0;
        int l = anIntArray551.length - 1;
        if (j == anIntArray551[k] || j == anIntArray551[l]) return true;
        do {
            int i1 = (k + l) / 2;
            if (j == anIntArray551[i1]) return true;
            if (j < anIntArray551[i1]) l = i1; else k = i1;
        } while (k != l && k + 1 != l);
        return false;
    }
