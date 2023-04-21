    private static void buildBoard() {
        int cnt = 0;
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < keyWord[i].length; j++) {
                cnt += keyWord[i][j].length;
            }
        }
        int[] tmp = new int[cnt / 2];
        int len = 0;
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < keyWord[i].length; j++) {
                for (int k = 0; k < keyWord[i][j].length; k += 2) {
                    tmp[len] = keyWord[i][j][k];
                    len++;
                }
            }
        }
        sort(tmp, len);
        first = new int[len];
        n = 0;
        for (int i = 0; i < len; ) {
            int j = i + 1;
            while (j < len && tmp[i] == tmp[j]) {
                j++;
            }
            first[n] = tmp[i];
            n++;
            i = j;
        }
        second = new int[n][cnt / 2];
        num = new int[n];
        for (int i = 0; i < n; i++) {
            num[i] = 0;
        }
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < keyWord[i].length; j++) {
                for (int k = 1; k < keyWord[i][j].length; k += 2) {
                    int l = 0, r = n - 1, op = 0;
                    while (l <= r) {
                        int mid = (l + r) / 2;
                        if (first[mid] == keyWord[i][j][k - 1]) {
                            op = mid;
                            break;
                        }
                        if (first[mid] > keyWord[i][j][k - 1]) {
                            r = mid - 1;
                        } else l = mid + 1;
                    }
                    second[op][num[op]] = keyWord[i][j][k];
                    num[op]++;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            sort(second[i], num[i]);
            int m = 0;
            for (int j = 0; j < num[i]; ) {
                int k = j + 1;
                while (k < num[i] && second[i][j] == second[i][k]) {
                    k++;
                }
                second[i][m] = second[i][j];
                m++;
                j = k;
            }
            num[i] = m;
        }
    }
