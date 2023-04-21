    public double getSimilarity() {
        double retVal;
        double prec;
        double rec;
        double fMstruct;
        double fMcont;
        prec = structuralSimilarity(false).getPrecision();
        rec = structuralSimilarity(false).getRecall();
        fMstruct = calculateFMeasure(prec, rec);
        System.out.println("Structural ");
        System.out.println(" p      : " + prec);
        System.out.println(" r      : " + rec);
        System.out.println(" fMesure: " + fMstruct);
        prec = contentSimilarity(false).getPrecision();
        rec = contentSimilarity(false).getRecall();
        fMcont = calculateFMeasure(prec, rec);
        System.out.println("Content ");
        System.out.println(" p      : " + prec);
        System.out.println(" r      : " + rec);
        System.out.println(" fMesure: " + fMcont);
        retVal = (fMstruct + fMcont) / 2;
        return retVal;
    }
