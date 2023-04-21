    @Override
    public void apply(double[] data) {
        int n = data.length;
        int l = 0, r = 0;
        for (int i = 0; i < n; i++) {
            if (data[i] != 0) {
                l = i;
                break;
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (data[i] != 0) {
                r = i;
                break;
            }
        }
        if (alignToLeft) for (int i = l + 1; i < r; i++) {
            if (data[i] == 0) {
                data[i] = data[i - 1];
            }
        } else for (int i = r - 1; i > l; i--) {
            if (data[i] == 0) {
                data[i] = data[i + 1];
            }
        }
    }
