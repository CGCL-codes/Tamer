    public void model() {
        int N = groups * players;
        int[] weights = new int[players];
        int base = Math.max(10, players + 1);
        weights[players - 1] = 1;
        for (int i = players - 2; i >= 0; i--) weights[i] = weights[i + 1] * base;
        System.out.println("Social golfer problem " + weeks + "-" + groups + "-" + players);
        store = new Store();
        golferGroup = new SetVar[weeks][groups];
        vars = new ArrayList<Var>();
        for (int i = 0; i < weeks; i++) for (int j = 0; j < groups; j++) {
            golferGroup[i][j] = new SetVar(store, "g_" + i + "_" + j, new BoundSetDomain(1, N));
            vars.add(golferGroup[i][j]);
            store.impose(new CardA(golferGroup[i][j], players));
        }
        for (int i = 0; i < weeks; i++) for (int j = 0; j < groups; j++) for (int k = j + 1; k < groups; k++) {
            store.impose(new AdisjointB(golferGroup[i][j], golferGroup[i][k]));
        }
        for (int i = 0; i < weeks; i++) {
            SetVar t = golferGroup[i][0];
            for (int j = 1; j < groups; j++) {
                SetVar r = new SetVar(store, "r-" + i + "-" + j, new BoundSetDomain(1, N));
                store.impose(new AunionBeqC(t, golferGroup[i][j], r));
                t = r;
            }
            store.impose(new AeqS(t, new IntervalDomain(1, N)));
        }
        for (int i = 0; i < weeks; i++) for (int j = i + 1; j < weeks; j++) if (i != j) for (int k = 0; k < groups; k++) for (int l = 0; l < groups; l++) {
            SetVar result = new SetVar(store, "res" + i + "-" + j + "-" + k + "-" + l, new BoundSetDomain(1, N));
            store.impose(new AintersectBeqC(golferGroup[i][k], golferGroup[j][l], result));
            store.impose(new CardA(result, 0, 1));
        }
        IntVar[] v = new IntVar[weeks];
        IntVar[][] var = new IntVar[weeks][players];
        for (int i = 0; i < weeks; i++) {
            v[i] = new IntVar(store, "v" + i, 0, 100000000);
            for (int j = 0; j < players; j++) var[i][j] = new IntVar(store, "var" + i + "-" + j, 1, N);
            store.impose(new Match(golferGroup[i][0], var[i]));
            store.impose(new SumWeight(var[i], weights, v[i]));
        }
        for (int i = 0; i < weeks - 1; i++) store.impose(new XlteqY(v[i], v[i + 1]));
    }
