    @SuppressWarnings("unchecked")
    private void sortOperationsByDate(List<Operation> operations, int lowerValue, int hightValue) throws Exception {
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortOperationsByDate(operations, lowerValue, pivot);
        sortOperationsByDate(operations, pivot + 1, hightValue);
        List<Operation> working = new ArrayList<Operation>();
        for (int i = 0; i < length; i++) working.add(i, operations.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    GDDate date = null;
                    GDDate date2 = null;
                    date = new GDDate(working.get(m1).getDateTime());
                    date2 = new GDDate(working.get(m2).getDateTime());
                    if (date.afterDay(date2)) {
                        operations.set(i + lowerValue, working.get(m2++));
                    } else {
                        operations.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    operations.set(i + lowerValue, working.get(m2++));
                }
            } else {
                operations.set(i + lowerValue, working.get(m1++));
            }
        }
    }
