    private void sortCitiesByName(List<City> citiesList, int lowerValue, int hightValue) throws Exception {
        Collator collator = Collator.getInstance(Locale.getDefault());
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortCitiesByName(citiesList, lowerValue, pivot);
        sortCitiesByName(citiesList, pivot + 1, hightValue);
        List<City> working = new ArrayList<City>();
        for (int i = 0; i < length; i++) working.add(i, citiesList.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    String stateName1 = working.get(m1).getCityName();
                    String stateName2 = working.get(m2).getCityName();
                    if (collator.compare(stateName1, stateName2) > 0) {
                        citiesList.set(i + lowerValue, working.get(m2++));
                    } else {
                        citiesList.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    citiesList.set(i + lowerValue, working.get(m2++));
                }
            } else {
                citiesList.set(i + lowerValue, working.get(m1++));
            }
        }
    }
