    private LinkedList<Datum> calculateBLUEsFromPhenotypes(MarkerPhenotypeAdapter mpa, String datasetName) {
        if (isInteractive()) {
            String msg = "The data set you have selected does not contain any marker data. Do you want to calculate BLUEs (best linear unbiased estimates) of the phenotypes?";
            String title = "Calculate BLUEs";
            int action = JOptionPane.showConfirmDialog(getParentFrame(), msg, title, JOptionPane.YES_NO_OPTION);
            if (action != JOptionPane.YES_OPTION) return null;
        }
        LinkedList<Datum> theResults = new LinkedList<Datum>();
        LinkedList<Object[]> anovaResults = new LinkedList<Object[]>();
        LinkedList<double[]> blueList = new LinkedList<double[]>();
        ArrayList<ArrayList<Object>> taxaListList = new ArrayList<ArrayList<Object>>();
        int numberOfCovariates = mpa.getNumberOfCovariates();
        int numberOfFactors = mpa.getNumberOfFactors();
        int numberOfPhenotypes = mpa.getNumberOfPhenotypes();
        for (int ph = 0; ph < numberOfPhenotypes; ph++) {
            double[] phenotypeData = mpa.getPhenotypeValues(ph);
            boolean[] missing = mpa.getMissingPhenotypes(ph);
            ArrayList<String[]> factorList = MarkerPhenotypeAdapterUtils.getFactorList(mpa, ph, missing);
            ArrayList<double[]> covariateList = MarkerPhenotypeAdapterUtils.getCovariateList(mpa, ph, missing);
            int[] nonmissingRows = MarkerPhenotypeAdapterUtils.getNonMissingIndex(missing);
            int numberOfObs = nonmissingRows.length;
            double[] y = new double[numberOfObs];
            for (int i = 0; i < numberOfObs; i++) {
                y[i] = phenotypeData[nonmissingRows[i]];
            }
            ArrayList<ModelEffect> modelEffects = new ArrayList<ModelEffect>();
            FactorModelEffect meanEffect = new FactorModelEffect(new int[numberOfObs], false);
            meanEffect.setID("mean");
            modelEffects.add(meanEffect);
            Identifier[] alltaxa = mpa.getTaxa(ph);
            Identifier[] taxa = new Identifier[numberOfObs];
            ArrayList<Object> taxaIds = new ArrayList<Object>();
            for (int i = 0; i < numberOfObs; i++) {
                taxa[i] = alltaxa[nonmissingRows[i]];
            }
            int[] taxaLevels = ModelEffectUtils.getIntegerLevels(taxa, taxaIds);
            taxaListList.add(taxaIds);
            FactorModelEffect taxaEffect = new FactorModelEffect(taxaLevels, true, "Taxa");
            modelEffects.add(taxaEffect);
            if (numberOfFactors > 0) {
                for (int f = 0; f < numberOfFactors; f++) {
                    String[] afactor = factorList.get(f);
                    String[] factorLabels = new String[numberOfObs];
                    for (int i = 0; i < numberOfObs; i++) factorLabels[i] = afactor[nonmissingRows[i]];
                    FactorModelEffect fme = new FactorModelEffect(ModelEffectUtils.getIntegerLevels(factorLabels), true, mpa.getFactorName(f));
                    modelEffects.add(fme);
                }
            }
            if (numberOfCovariates > 0) {
                for (int c = 0; c < numberOfCovariates; c++) {
                    double[] covar = new double[numberOfObs];
                    double[] covariateData = covariateList.get(c);
                    for (int i = 0; i < numberOfObs; i++) covar[i] = covariateData[nonmissingRows[i]];
                    modelEffects.add(new CovariateModelEffect(covar, mpa.getCovariateName(c)));
                }
            }
            SweepFastLinearModel sflm = new SweepFastLinearModel(modelEffects, y);
            double[] taxaSSdf = sflm.getMarginalSSdf(1);
            double[] modelSSdf = sflm.getFullModelSSdf();
            double[] errorSSdf = sflm.getResidualSSdf();
            double[] beta = sflm.getBeta();
            double F, p;
            F = taxaSSdf[0] / taxaSSdf[1] / errorSSdf[0] * errorSSdf[1];
            try {
                p = LinearModelUtils.Ftest(F, taxaSSdf[1], errorSSdf[1]);
            } catch (Exception e) {
                p = Double.NaN;
            }
            Object[] result = new Object[9];
            result[0] = mpa.getPhenotypeName(ph);
            result[1] = new Double(F);
            result[2] = new Double(p);
            result[3] = new Double(taxaSSdf[1]);
            result[4] = new Double(taxaSSdf[0] / taxaSSdf[1]);
            result[5] = new Double(errorSSdf[1]);
            result[6] = new Double(errorSSdf[0] / errorSSdf[1]);
            result[7] = new Double(modelSSdf[1]);
            result[8] = new Double(modelSSdf[0] / modelSSdf[1]);
            anovaResults.add(result);
            double overallMean = beta[0];
            int nEffects = modelEffects.size();
            int start = 0;
            for (int i = 1; i < nEffects; i++) {
                ModelEffect me = modelEffects.get(i);
                if (me instanceof FactorModelEffect && !me.getID().equals("Taxa")) {
                    FactorModelEffect fme = (FactorModelEffect) me;
                    int nLevels = fme.getNumberOfLevels();
                    int nEstimates;
                    if (fme.getRestricted()) {
                        nEstimates = nLevels - 1;
                    } else {
                        nEstimates = nLevels;
                    }
                    double factorMean = 0;
                    for (int j = 0; j < nEstimates; j++) {
                        factorMean += beta[j + start];
                    }
                    factorMean /= nLevels;
                    overallMean += factorMean;
                    start += nEstimates;
                } else {
                    start += me.getNumberOfLevels();
                }
            }
            int n = taxaIds.size();
            double[] blues = new double[n];
            for (int i = 0; i < n - 1; i++) {
                blues[i] = beta[i + 1] + overallMean;
            }
            blues[n - 1] = overallMean;
            blueList.add(blues);
            taxaListList.add(taxaIds);
        }
        String[] anovaColumnLabels = new String[] { "Trait", "F", "p", "taxaDF", "taxaMS", "errorDF", "errorMS", "modelDF", "modelMS" };
        Object[][] table = new Object[anovaResults.size()][];
        anovaResults.toArray(table);
        String datumName = "Phenotype ANOVA from " + datasetName;
        StringBuilder datumComments = new StringBuilder("ANOVA for Phenotypes using GLM\n");
        datumComments.append("Data set: ").append(datasetName);
        datumComments.append("\nmodel: trait = mean + taxa");
        for (int i = 0; i < mpa.getNumberOfFactors(); i++) {
            datumComments.append(" + ");
            datumComments.append(mpa.getFactorName(i));
        }
        for (int i = 0; i < mpa.getNumberOfCovariates(); i++) {
            datumComments.append(" + ");
            datumComments.append(mpa.getCovariateName(i));
        }
        SimpleTableReport str = new SimpleTableReport(datumName, anovaColumnLabels, table);
        Datum theAnova = new Datum(datumName, str, datumComments.toString());
        theResults.add(theAnova);
        TreeSet<Identifier> taxaSet = new TreeSet<Identifier>();
        for (ArrayList<Object> list : taxaListList) {
            for (Object taxon : list) taxaSet.add((Identifier) taxon);
        }
        HashMap<Identifier, Integer> taxaMap = new HashMap<Identifier, Integer>();
        int count = 0;
        for (Identifier taxon : taxaSet) {
            taxaMap.put(taxon, count++);
        }
        String[] blueColumnLabels = new String[numberOfPhenotypes + 1];
        blueColumnLabels[0] = "Taxa";
        for (int i = 0; i < numberOfPhenotypes; i++) {
            blueColumnLabels[i + 1] = mpa.getPhenotypeName(i);
        }
        int nrows = taxaSet.size();
        double[][] blues = new double[nrows][numberOfPhenotypes];
        for (int r = 0; r < nrows; r++) {
            for (int c = 0; c < numberOfPhenotypes; c++) {
                blues[r][c] = Double.NaN;
            }
        }
        LinkedList<Trait> traitList = new LinkedList<Trait>();
        for (int c = 0; c < numberOfPhenotypes; c++) {
            traitList.add(new Trait(mpa.getPhenotypeName(c), false, Trait.TYPE_DATA));
            double[] pheno = blueList.get(c);
            int n = pheno.length;
            ArrayList<Object> taxaList = taxaListList.get(c);
            for (int i = 0; i < n; i++) {
                int ndx = taxaMap.get(taxaList.get(i));
                if (ndx > -1) blues[ndx][c] = pheno[i];
            }
        }
        Identifier[] taxaIds = new Identifier[taxaSet.size()];
        taxaSet.toArray(taxaIds);
        Phenotype thePhenotype = new SimplePhenotype(new SimpleIdGroup(taxaIds), traitList, blues);
        theResults.add(new Datum("BLUEs_" + datasetName, thePhenotype, "BLUEs calculated from " + datasetName));
        return theResults;
    }
