    protected double[] solveLGS() {
        double[] a1 = new double[a.length - 1];
        double[] b1 = new double[b.length - 1];
        for (int i = 0; i < a1.length; i++) {
            a1[i] = a[i + 1];
            b1[i] = b[i];
        }
        double[] twoTmp = new double[a1.length + 1];
        for (int i = 0; i < twoTmp.length; i++) {
            twoTmp[i] = 2;
        }
        double[] res = xxl.core.math.Maths.triDiagonalGaussianLGS(a1, twoTmp, b1, rightSide);
        double[] result = new double[res.length + 2];
        result[0] = result[result.length - 1] = 0;
        for (int i = 0; i < res.length; i++) {
            result[i + 1] = res[i];
        }
        return result;
    }
