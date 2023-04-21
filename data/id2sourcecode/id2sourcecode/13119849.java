    private void sortPresencesByNameAndDateTime(List<Presence> presencesList, int lowerValue, int hightValue) throws Exception {
        Collator collator = Collator.getInstance(Locale.getDefault());
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortPresencesByNameAndDateTime(presencesList, lowerValue, pivot);
        sortPresencesByNameAndDateTime(presencesList, pivot + 1, hightValue);
        List<Presence> working = new ArrayList<Presence>();
        for (int i = 0; i < length; i++) working.add(i, presencesList.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    String name = working.get(m1).getRegistrationAppointment().getRegistration().getStudent().getName();
                    String name2 = working.get(m2).getRegistrationAppointment().getRegistration().getStudent().getName();
                    if (collator.compare(name, name2) > 0) {
                        presencesList.set(i + lowerValue, working.get(m2++));
                    } else {
                        if (name.equals(name2)) {
                            GDDate date = new GDDate(working.get(m1).getDate());
                            GDDate date2 = new GDDate(working.get(m2).getDate());
                            if (date.after(date2)) {
                                presencesList.set(i + lowerValue, working.get(m2++));
                            } else {
                                presencesList.set(i + lowerValue, working.get(m1++));
                            }
                        } else {
                            presencesList.set(i + lowerValue, working.get(m1++));
                        }
                    }
                } else {
                    presencesList.set(i + lowerValue, working.get(m2++));
                }
            } else {
                presencesList.set(i + lowerValue, working.get(m1++));
            }
        }
    }
