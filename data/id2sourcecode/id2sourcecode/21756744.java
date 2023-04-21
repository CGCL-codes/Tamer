    public LinearNormalizer(Collection<PeakListRow> standards, Dataset input) {
        if (standards.size() == 0) {
            throw new IllegalArgumentException("No standards given!");
        }
        ends = new double[standards.size()];
        baseLevel = BASE_LEVEL;
        if (standards.size() > 1) {
            onlyStandard = null;
            ArrayList<PeakListRow> tempRows = new ArrayList<PeakListRow>(standards);
            sort(tempRows);
            _standards = tempRows;
            for (int i = 0; i < tempRows.size(); i++) {
                if (tempRows.get(i).getClass().toString().contains("GCGC")) {
                    double curPoint = ((SimplePeakListRowGCGC) tempRows.get(i)).getRT1();
                    double nextPoint = (i == tempRows.size() - 1 ? Double.POSITIVE_INFINITY : ((SimplePeakListRowGCGC) tempRows.get(i + 1)).getRT1());
                    double end = (curPoint + nextPoint) / 2;
                    ends[i] = end;
                } else if (tempRows.get(i).getClass().toString().contains("LCMS")) {
                    double curPoint = ((SimplePeakListRowLCMS) tempRows.get(i)).getRT();
                    double nextPoint = (i == tempRows.size() - 1 ? Double.POSITIVE_INFINITY : ((SimplePeakListRowLCMS) tempRows.get(i + 1)).getRT());
                    double end = (curPoint + nextPoint) / 2;
                    ends[i] = end;
                }
            }
        } else if (standards.size() == 1) {
            _standards = null;
            Iterator<PeakListRow> i = standards.iterator();
            onlyStandard = i.next();
        } else {
            throw new IllegalArgumentException("Empty standard list");
        }
        _total = input == null ? 0 : input.getNumberRows();
    }
