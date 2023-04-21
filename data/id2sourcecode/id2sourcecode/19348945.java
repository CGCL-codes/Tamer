    public void step5() {
        int len = k - l + 1;
        boolean repeated = true;
        for (int i = l; i < k; i++) {
            if (b[i] == b[i + 1]) {
                for (int j = i + 1; j < k; j++) b[j] = b[j + 1];
                k--;
                break;
            }
        }
    }
