    public static int[] src(int[] op, int lr, int al, int count) {
        int len = op.length;
        int[] res = new int[len];
        if (lr == 0) {
            if (al == 0) {
                for (int i = 0; i < count; i++) {
                    for (int j = len - 1; j > 1; j--) {
                        res[j - 1] = op[j];
                    }
                    res[1] = 0;
                }
            } else {
                for (int i = 0; i < count; i++) {
                    for (int j = len - 1; j > 0; j--) {
                        res[j - 1] = op[j];
                    }
                    res[0] = 0;
                }
            }
        } else {
            if (al == 0) {
                for (int i = 0; i < count; i++) {
                    for (int j = 1; j < len - 1; j++) {
                        res[j] = op[j + 1];
                    }
                    res[len - 1] = 0;
                }
            } else {
                for (int i = 0; i < count; i++) {
                    for (int j = 0; j < len - 1; j++) {
                        res[j] = op[j + 1];
                    }
                    res[len - 1] = 0;
                }
            }
        }
        return res;
    }
