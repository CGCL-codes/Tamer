    private void sortRegistrationByName(List<Registration> registrationsData, int lowerValue, int hightValue) throws Exception {
        Collator collator = Collator.getInstance(Locale.getDefault());
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortRegistrationByName(registrationsData, lowerValue, pivot);
        sortRegistrationByName(registrationsData, pivot + 1, hightValue);
        List<Registration> working = new ArrayList<Registration>();
        for (int i = 0; i < length; i++) working.add(i, registrationsData.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    String name = working.get(m1).getStudent().getName();
                    String name2 = working.get(m2).getStudent().getName();
                    if (collator.compare(name, name2) > 0) {
                        registrationsData.set(i + lowerValue, working.get(m2++));
                    } else {
                        registrationsData.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    registrationsData.set(i + lowerValue, working.get(m2++));
                }
            } else {
                registrationsData.set(i + lowerValue, working.get(m1++));
            }
        }
    }
