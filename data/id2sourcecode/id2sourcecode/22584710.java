    public static int[] rrc(int[] op, int lr, int al, int count) {
        int tmp = 0;
        int len = op.length;
        int[] res = new int[len];
        if (lr == 0) {
            if (al == 0) {
                for (int i = 0; i < count; i++) {
                    tmp = op[len - 1];
                    for (int j = len - 1; j > 1; j--) {
                        res[j - 1] = op[j];
                    }
                    res[1] = tmp;
                }
            } else {
                for (int i = 0; i < count; i++) {
                    tmp = op[len - 1];
                    for (int j = len - 1; j > 0; j--) {
                        res[j - 1] = op[j];
                    }
                    res[0] = tmp;
                }
            }
        } else {
            if (al == 0) {
                for (int i = 0; i < count; i++) {
                    tmp = op[1];
                    for (int j = 1; j < len - 1; j++) {
                        res[j] = op[j + 1];
                    }
                    res[len - 1] = tmp;
                }
            } else {
                for (int i = 0; i < count; i++) {
                    tmp = op[0];
                    for (int j = 0; j < len - 1; j++) {
                        res[j] = op[j + 1];
                    }
                    res[len - 1] = tmp;
                }
            }
        }
        return res;
    }
