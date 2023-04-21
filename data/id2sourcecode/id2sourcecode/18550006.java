    @Override
    public void model() {
        System.out.println("Creating model for solution with " + numberInnerMoves + " intermediate steps");
        store = new Store();
        vars = new ArrayList<Var>();
        IntVar left = new IntVar(store, "left", 0, 0);
        IntVar right = new IntVar(store, "right", 2, 2);
        IntVar[] wolf = new IntVar[2 + numberInnerMoves];
        IntVar[] goat = new IntVar[2 + numberInnerMoves];
        IntVar[] cabbage = new IntVar[2 + numberInnerMoves];
        wolf[0] = goat[0] = cabbage[0] = left;
        wolf[numberInnerMoves + 1] = goat[numberInnerMoves + 1] = cabbage[numberInnerMoves + 1] = right;
        for (int i = 1; i < numberInnerMoves + 1; i++) {
            wolf[i] = new IntVar(store, "wolfStateInMove" + i, 0, 2);
            goat[i] = new IntVar(store, "goatStateInMove" + i, 0, 2);
            cabbage[i] = new IntVar(store, "cabbageStateInMove" + i, 0, 2);
            vars.add(wolf[i]);
            vars.add(goat[i]);
            vars.add(cabbage[i]);
        }
        int[][] allowedTransitions = { { 0, 1, 0 }, { 1, 0, 0 }, { 2, 1, 2 }, { 1, 2, 2 }, { 0, 0, 0 }, { 0, 0, 2 }, { 2, 2, 0 }, { 2, 2, 2 } };
        for (int i = 0; i < numberInnerMoves + 1; i++) {
            IntVar[] temp = { wolf[i], wolf[i + 1], null };
            if (i % 2 == 0) temp[2] = left; else temp[2] = right;
            store.impose(new ExtensionalSupportVA(temp, allowedTransitions));
            temp[0] = goat[i];
            temp[1] = goat[i + 1];
            store.impose(new ExtensionalSupportVA(temp, allowedTransitions));
            temp[0] = cabbage[i];
            temp[1] = cabbage[i + 1];
            store.impose(new ExtensionalSupportVA(temp, allowedTransitions));
        }
        IntVar[] bw = new IntVar[numberInnerMoves];
        IntVar[] bg = new IntVar[numberInnerMoves];
        IntVar[] bc = new IntVar[numberInnerMoves];
        for (int i = 1; i < numberInnerMoves + 1; i++) {
            bw[i - 1] = new IntVar(store, "wolfOnBoatInMove" + i, 0, 1);
            bg[i - 1] = new IntVar(store, "goatOnBoatInMove" + i, 0, 1);
            bc[i - 1] = new IntVar(store, "cabbageOnBoatInMove" + i, 0, 1);
            store.impose(new Reified(new XeqC(wolf[i], 1), bw[i - 1]));
            store.impose(new Reified(new XeqC(goat[i], 1), bg[i - 1]));
            store.impose(new Reified(new XeqC(cabbage[i], 1), bc[i - 1]));
            IntVar[] b = { bw[i - 1], bg[i - 1], bc[i - 1] };
            IntVar numberOnBoat = new IntVar(store, "numberOnBoatInMove" + i, 0, 1);
            store.impose(new Sum(b, numberOnBoat));
            store.impose(new XneqY(wolf[i], goat[i]));
            store.impose(new XneqY(goat[i], cabbage[i]));
        }
    }
