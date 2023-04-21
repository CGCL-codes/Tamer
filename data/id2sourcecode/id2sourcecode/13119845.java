    @SuppressWarnings("unchecked")
    private void sortPresencesByDate(List<Presence> presences, int lowerValue, int hightValue) throws Exception {
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortPresencesByDate(presences, lowerValue, pivot);
        sortPresencesByDate(presences, pivot + 1, hightValue);
        List<Presence> working = new ArrayList<Presence>();
        for (int i = 0; i < length; i++) working.add(i, presences.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    GDDate date = null;
                    GDDate date2 = null;
                    date = new GDDate(working.get(m1).getDate());
                    date2 = new GDDate(working.get(m2).getDate());
                    if (date.afterDay(date2)) {
                        presences.set(i + lowerValue, working.get(m2++));
                    } else {
                        presences.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    presences.set(i + lowerValue, working.get(m2++));
                }
            } else {
                presences.set(i + lowerValue, working.get(m1++));
            }
        }
    }
