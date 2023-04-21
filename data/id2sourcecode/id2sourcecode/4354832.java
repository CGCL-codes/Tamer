    private void merge(int s, int l, double[] temp) {
        int ss = s;
        int m = (l + s) / 2;
        int ll = m + 1;
        int i = s;
        while (ss <= m && ll <= l) {
            if (values[ss] <= values[ll]) {
                temp[i] = values[ss];
                ss++;
                i++;
            } else {
                temp[i] = values[ll];
                ll++;
                i++;
            }
        }
        while (ss <= m) {
            temp[i] = values[ss];
            i++;
            ss++;
        }
        while (ll <= l) {
            temp[i] = values[ll];
            i++;
            ll++;
        }
        for (i = s; i <= l; i++) {
            values[i] = temp[i];
        }
    }
