    @Override
    public double compute(Object _caseList, Object _comparisionList) throws NoApplicableSimilarityFunctionException {
        double countEquals = 0;
        if ((_caseList == null) || (_comparisionList == null)) return 0;
        ListComparator caseList = (ListComparator) _caseList;
        ListComparator comparisionList = (ListComparator) _comparisionList;
        for (Bean baseItem : caseList.getListToComparate()) {
            for (Bean comparitionItem : comparisionList.getListToComparate()) {
                if (baseItem.equals(comparitionItem)) {
                    countEquals++;
                    break;
                }
            }
        }
        double factorBase = countEquals / comparisionList.getListToComparate().size();
        double factorSource = countEquals / caseList.getListToComparate().size();
        double factorSimilitud = (factorBase + factorSource) / 2;
        return factorSimilitud;
    }
