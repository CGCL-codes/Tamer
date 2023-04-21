    private void sortRegistrationById(List<Registration> registrationsID, int lowerValue, int hightValue) throws Exception {
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortRegistrationById(registrationsID, lowerValue, pivot);
        sortRegistrationById(registrationsID, pivot + 1, hightValue);
        List<Registration> working = new ArrayList<Registration>();
        for (int i = 0; i < length; i++) working.add(i, registrationsID.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    Integer id = new Integer(working.get(m1).getIdRegistration());
                    Integer id2 = new Integer(working.get(m2).getIdRegistration());
                    if (id.compareTo(id2) > 0) {
                        registrationsID.set(i + lowerValue, working.get(m2++));
                    } else {
                        registrationsID.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    registrationsID.set(i + lowerValue, working.get(m2++));
                }
            } else {
                registrationsID.set(i + lowerValue, working.get(m1++));
            }
        }
    }
