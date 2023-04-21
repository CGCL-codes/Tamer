    public static double findRoot(Funcion f, double a, double b, double error, int iterations) throws RaizNoEncontradaExcepcion {
        int i = 1;
        double fa = f.eval(a);
        while (i <= iterations) {
            double p = a + (b - a) / 2;
            double fp = f.eval(p);
            if (fp == 0 || (b - a) / 2 < error) {
                return p;
            }
            i += 1;
            if (fa * fp > 0) {
                a = p;
                fa = fp;
            } else {
                b = p;
            }
        }
        throw new RaizNoEncontradaExcepcion(METHOD_NAME);
    }
