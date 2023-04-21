    private int getInd0(double[] arr, int nP, double x) {
        int ind0 = 0;
        int ind1 = nP - 1;
        if (x <= arr[ind0]) {
            return ind0;
        }
        if (x >= arr[ind1]) {
            return (ind1 - 1);
        }
        int ind = 0;
        while ((ind1 - ind0) != 1) {
            ind = (ind1 + ind0) / 2;
            if (arr[ind] > x && arr[ind0] <= x) {
                ind1 = ind;
            } else {
                ind0 = ind;
            }
        }
        return ind0;
    }
