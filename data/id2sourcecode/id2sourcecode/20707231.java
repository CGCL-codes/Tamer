    public FrequencyNode(ProbabilityFunction func) {
        DiscreteVariable[] vars = func.get_variables();
        id = vars[0].get_index();
        int maxK = vars[0].number_values();
        if (vars.length > 1) {
            parents = new int[vars.length - 1];
            int maxJ = 1;
            for (int p = 0; p < parents.length; ++p) {
                parents[p] = vars[p + 1].get_index();
                maxJ *= vars[p + 1].number_values();
            }
            parentMult = new int[vars.length - 1];
            nJ = new int[maxJ];
            nJK = new int[maxJ][maxK];
            for (int p = 0; p < parentMult.length; ++p) {
                maxJ /= vars[p + 1].number_values();
                parentMult[p] = maxJ;
            }
        } else {
            parents = null;
            parentMult = null;
            nJ = new int[1];
            nJK = new int[1][maxK];
        }
    }
