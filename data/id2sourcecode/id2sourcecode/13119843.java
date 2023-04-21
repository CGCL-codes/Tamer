    @SuppressWarnings("unchecked")
    private void sortOperationsById(List<Operation> operations, int lowerValue, int hightValue) throws Exception {
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortOperationsById(operations, lowerValue, pivot);
        sortOperationsById(operations, pivot + 1, hightValue);
        List<Operation> working = new ArrayList<Operation>();
        for (int i = 0; i < length; i++) working.add(i, operations.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    int id = Integer.parseInt(working.get(m1).getId());
                    int id2 = Integer.parseInt(working.get(m2).getId());
                    if (id > id2) {
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
