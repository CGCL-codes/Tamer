    private void z() {
        for (int i = 0; i < 125; i++) {
            for (int j = 0; j < 125; j++) {
                pt0[0] = i;
                pt0[1] = hh[i][j];
                pt0[2] = j;
                pt1[0] = i + 1;
                pt1[1] = hh[i + 1][j];
                pt1[2] = j;
                pt2[0] = i;
                pt2[1] = hh[i][j + 1];
                pt2[2] = j + 1;
                pt3[0] = i + 1;
                pt3[1] = hh[i + 1][j + 1];
                pt3[2] = j + 1;
                sub(pt0, pt1, a);
                sub(pt2, pt1, b);
                sub(pt3, pt1, c);
                cross(a, b, n0);
                norm(n0);
                cross(b, c, n1);
                norm(n1);
                copy(n0, fn[0][i][j]);
                copy(n1, fn[1][i][j]);
            }
        }
    }
