    public static void divide(int[] zds, int nx, int[] y, int ny) {
        int j = nx;
        do {
            int qhat;
            if (zds[j] == y[ny - 1]) qhat = -1; else {
                long w = (((long) (zds[j])) << 32) + ((long) zds[j - 1] & 0xffffffffL);
                qhat = (int) udiv_qrnnd(w, y[ny - 1]);
            }
            if (qhat != 0) {
                int borrow = submul_1(zds, j - ny, y, ny, qhat);
                int save = zds[j];
                long num = ((long) save & 0xffffffffL) - ((long) borrow & 0xffffffffL);
                while (num != 0) {
                    qhat--;
                    long carry = 0;
                    for (int i = 0; i < ny; i++) {
                        carry += ((long) zds[j - ny + i] & 0xffffffffL) + ((long) y[i] & 0xffffffffL);
                        zds[j - ny + i] = (int) carry;
                        carry >>>= 32;
                    }
                    zds[j] += carry;
                    num = carry - 1;
                }
            }
            zds[j] = qhat;
        } while (--j >= ny);
    }
