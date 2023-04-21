    private void generateClassifierForNode(Instances data, Range classes, Random rand, Classifier classifier, Hashtable table, double[] instsNumAllClasses) throws Exception {
        int[] indices = classes.getSelection();
        for (int j = indices.length - 1; j > 0; j--) {
            int randPos = rand.nextInt(j + 1);
            int temp = indices[randPos];
            indices[randPos] = indices[j];
            indices[j] = temp;
        }
        double total = 0;
        for (int j = 0; j < indices.length; j++) {
            total += instsNumAllClasses[indices[j]];
        }
        double halfOfTotal = total / 2;
        double sumLeft = 0, sumRight = 0;
        int i = 0, j = indices.length - 1;
        do {
            if (i == j) {
                if (rand.nextBoolean()) {
                    sumLeft += instsNumAllClasses[indices[i++]];
                } else {
                    sumRight += instsNumAllClasses[indices[j--]];
                }
            } else {
                sumLeft += instsNumAllClasses[indices[i++]];
                sumRight += instsNumAllClasses[indices[j--]];
            }
        } while (Utils.sm(sumLeft, halfOfTotal) && Utils.sm(sumRight, halfOfTotal));
        int first = 0, second = 0;
        if (!Utils.sm(sumLeft, halfOfTotal)) {
            first = i;
        } else {
            first = j + 1;
        }
        second = indices.length - first;
        int[] firstInds = new int[first];
        int[] secondInds = new int[second];
        System.arraycopy(indices, 0, firstInds, 0, first);
        System.arraycopy(indices, first, secondInds, 0, second);
        int[] sortedFirst = Utils.sort(firstInds);
        int[] sortedSecond = Utils.sort(secondInds);
        int[] firstCopy = new int[first];
        int[] secondCopy = new int[second];
        for (int k = 0; k < sortedFirst.length; k++) {
            firstCopy[k] = firstInds[sortedFirst[k]];
        }
        firstInds = firstCopy;
        for (int k = 0; k < sortedSecond.length; k++) {
            secondCopy[k] = secondInds[sortedSecond[k]];
        }
        secondInds = secondCopy;
        if (firstInds[0] > secondInds[0]) {
            int[] help = secondInds;
            secondInds = firstInds;
            firstInds = help;
            int help2 = second;
            second = first;
            first = help2;
        }
        m_Range = new Range(Range.indicesToRangeList(firstInds));
        m_Range.setUpper(data.numClasses() - 1);
        Range secondRange = new Range(Range.indicesToRangeList(secondInds));
        secondRange.setUpper(data.numClasses() - 1);
        MakeIndicator filter = new MakeIndicator();
        filter.setAttributeIndex("" + (data.classIndex() + 1));
        filter.setValueIndices(m_Range.getRanges());
        filter.setNumeric(false);
        filter.setInputFormat(data);
        m_FilteredClassifier = new FilteredClassifier();
        if (data.numInstances() > 0) {
            m_FilteredClassifier.setClassifier(Classifier.makeCopies(classifier, 1)[0]);
        } else {
            m_FilteredClassifier.setClassifier(new weka.classifiers.rules.ZeroR());
        }
        m_FilteredClassifier.setFilter(filter);
        m_classifiers = table;
        if (!m_classifiers.containsKey(getString(firstInds) + "|" + getString(secondInds))) {
            m_FilteredClassifier.buildClassifier(data);
            m_classifiers.put(getString(firstInds) + "|" + getString(secondInds), m_FilteredClassifier);
        } else {
            m_FilteredClassifier = (FilteredClassifier) m_classifiers.get(getString(firstInds) + "|" + getString(secondInds));
        }
        m_FirstSuccessor = new DataNearBalancedND();
        if (first == 1) {
            m_FirstSuccessor.m_Range = m_Range;
        } else {
            RemoveWithValues rwv = new RemoveWithValues();
            rwv.setInvertSelection(true);
            rwv.setNominalIndices(m_Range.getRanges());
            rwv.setAttributeIndex("" + (data.classIndex() + 1));
            rwv.setInputFormat(data);
            Instances firstSubset = Filter.useFilter(data, rwv);
            m_FirstSuccessor.generateClassifierForNode(firstSubset, m_Range, rand, classifier, m_classifiers, instsNumAllClasses);
        }
        m_SecondSuccessor = new DataNearBalancedND();
        if (second == 1) {
            m_SecondSuccessor.m_Range = secondRange;
        } else {
            RemoveWithValues rwv = new RemoveWithValues();
            rwv.setInvertSelection(true);
            rwv.setNominalIndices(secondRange.getRanges());
            rwv.setAttributeIndex("" + (data.classIndex() + 1));
            rwv.setInputFormat(data);
            Instances secondSubset = Filter.useFilter(data, rwv);
            m_SecondSuccessor = new DataNearBalancedND();
            m_SecondSuccessor.generateClassifierForNode(secondSubset, secondRange, rand, classifier, m_classifiers, instsNumAllClasses);
        }
    }
