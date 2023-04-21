    public long[] indexOfRouth(C a, C b, GenPolynomial<C> f, GenPolynomial<C> g) {
        List<GenPolynomial<C>> S = sturmSequence(f, g);
        RingFactory<C> cfac = f.ring.coFac;
        List<C> l = PolyUtil.<C>evaluateMain(cfac, S, a);
        List<C> r = PolyUtil.<C>evaluateMain(cfac, S, b);
        long v = RootUtil.<C>signVar(l) - RootUtil.<C>signVar(r);
        long d = f.degree(0);
        if (d < g.degree(0)) {
            d = g.degree(0);
        }
        long ui = (d - v) / 2;
        long li = (d + v) / 2;
        return new long[] { ui, li };
    }
