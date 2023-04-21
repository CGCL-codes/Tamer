    private void sortRegistrationAppointmentByName(List<RegistrationAppointment> registrationList, int lowerValue, int hightValue) throws Exception {
        Collator collator = Collator.getInstance(Locale.getDefault());
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortRegistrationAppointmentByName(registrationList, lowerValue, pivot);
        sortRegistrationAppointmentByName(registrationList, pivot + 1, hightValue);
        List<RegistrationAppointment> working = new ArrayList<RegistrationAppointment>();
        for (int i = 0; i < length; i++) working.add(i, registrationList.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    String name = working.get(m1).getRegistration().getStudent().getName();
                    String name2 = working.get(m2).getRegistration().getStudent().getName();
                    if (collator.compare(name, name2) > 0) {
                        registrationList.set(i + lowerValue, working.get(m2++));
                    } else {
                        registrationList.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    registrationList.set(i + lowerValue, working.get(m2++));
                }
            } else {
                registrationList.set(i + lowerValue, working.get(m1++));
            }
        }
    }
