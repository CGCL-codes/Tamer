    private void sortRegistrationByDateOfLastParcel(List<Registration> registrationsData, int lowerValue, int hightValue) throws Exception {
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortRegistrationByDateOfLastParcel(registrationsData, lowerValue, pivot);
        sortRegistrationByDateOfLastParcel(registrationsData, pivot + 1, hightValue);
        List<Registration> working = new ArrayList<Registration>();
        for (int i = 0; i < length; i++) working.add(i, registrationsData.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    GDDate payDate = new GDDate(working.get(m1).getParcel(working.get(m1).getParcels().size() - 1).getDate());
                    GDDate payDate2 = new GDDate(working.get(m2).getParcel(working.get(m2).getParcels().size() - 1).getDate());
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
