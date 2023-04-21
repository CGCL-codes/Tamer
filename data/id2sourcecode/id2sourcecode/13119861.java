    private void sortStateByName(List<State> statesList, int lowerValue, int hightValue) throws Exception {
        Collator collator = Collator.getInstance(Locale.getDefault());
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortStateByName(statesList, lowerValue, pivot);
        sortStateByName(statesList, pivot + 1, hightValue);
        List<State> working = new ArrayList<State>();
        for (int i = 0; i < length; i++) working.add(i, statesList.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    String stateName1 = working.get(m1).getAcronym();
                    String stateName2 = working.get(m2).getAcronym();
                    if (collator.compare(stateName1, stateName2) > 0) {
                        statesList.set(i + lowerValue, working.get(m2++));
                    } else {
                        statesList.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    statesList.set(i + lowerValue, working.get(m2++));
                }
            } else {
                statesList.set(i + lowerValue, working.get(m1++));
            }
        }
    }
