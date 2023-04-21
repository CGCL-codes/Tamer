    private void sortFlowBankAccountDataPerDate(List<List<String>> flowBankAccountDataList, int lowerValue, int hightValue) throws Exception {
        Collator collator = Collator.getInstance(Locale.getDefault());
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortFlowBankAccountDataPerDate(flowBankAccountDataList, lowerValue, pivot);
        sortFlowBankAccountDataPerDate(flowBankAccountDataList, pivot + 1, hightValue);
        List<List<String>> working = new ArrayList<List<String>>();
        for (int i = 0; i < length; i++) working.add(i, flowBankAccountDataList.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    GDDate date1 = new GDDate(working.get(m1).get(1));
                    GDDate date2 = new GDDate(working.get(m2).get(1));
                    if (date1.afterDay(date2)) {
                        flowBankAccountDataList.set(i + lowerValue, working.get(m2++));
                    } else {
                        flowBankAccountDataList.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    flowBankAccountDataList.set(i + lowerValue, working.get(m2++));
                }
            } else {
                flowBankAccountDataList.set(i + lowerValue, working.get(m1++));
            }
        }
    }
