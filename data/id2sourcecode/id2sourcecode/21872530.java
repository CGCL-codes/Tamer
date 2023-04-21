    public DependencyInstance parseSentence(DependencyInstance instance) {
        String[] forms = instance.forms;
        FeatureVector[][][] fvs = new FeatureVector[forms.length][forms.length][2];
        double[][][] probs = new double[forms.length][forms.length][2];
        FeatureVector[][][][] nt_fvs = new FeatureVector[forms.length][pipe.types.length][2][2];
        double[][][][] nt_probs = new double[forms.length][pipe.types.length][2][2];
        pipe.fillFeatureVectors(instance, fvs, probs, nt_fvs, nt_probs, params);
        int K = 1;
        Object[][] d = null;
        d = decoder.decodeNonProjective(instance, fvs, probs, nt_fvs, nt_probs, K);
        String[] res = ((String) d[0][1]).split(" ");
        String[] pos = instance.cpostags;
        String[] formsNoRoot = new String[forms.length - 1];
        String[] posNoRoot = new String[formsNoRoot.length];
        String[] labels = new String[formsNoRoot.length];
        int[] heads = new int[formsNoRoot.length];
        for (int j = 0; j < formsNoRoot.length; j++) {
            formsNoRoot[j] = forms[j + 1];
            posNoRoot[j] = pos[j + 1];
            String[] trip = res[j].split("[\\|:]");
            labels[j] = pipe.types[Integer.parseInt(trip[2])];
            heads[j] = Integer.parseInt(trip[0]);
        }
        DependencyInstance di = new DependencyInstance(formsNoRoot, posNoRoot, labels, heads);
        return di;
    }
