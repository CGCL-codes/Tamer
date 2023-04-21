    private void sortRegistrationByEndDate(List<Registration> registrationsData, int lowerValue, int hightValue) throws Exception {
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortRegistrationByEndDate(registrationsData, lowerValue, pivot);
        sortRegistrationByEndDate(registrationsData, pivot + 1, hightValue);
        List<Registration> working = new ArrayList<Registration>();
        for (int i = 0; i < length; i++) working.add(i, registrationsData.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    GDDate payDate = working.get(m1).getEndDate();
                    GDDate payDate2 = working.get(m2).getEndDate();
                    if (payDate.afterDay(payDate2)) {
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
