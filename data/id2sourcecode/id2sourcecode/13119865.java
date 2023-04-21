    private void sortBankBilletByDateAndName(List<Billet> billetsList, int lowerValue, int hightValue) throws Exception {
        Collator collator = Collator.getInstance(Locale.getDefault());
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortBankBilletByDateAndName(billetsList, lowerValue, pivot);
        sortBankBilletByDateAndName(billetsList, pivot + 1, hightValue);
        List<Billet> working = new ArrayList<Billet>();
        for (int i = 0; i < length; i++) working.add(i, billetsList.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    long date1 = new GDDate(working.get(m1).getParcel().getDate()).getTimeInMillis();
                    long date2 = new GDDate(working.get(m2).getParcel().getDate()).getTimeInMillis();
                    String name1 = working.get(m1).getPerson().getName();
                    String name2 = working.get(m2).getPerson().getName();
                    if (collator.compare(name1, name2) > 0) {
                        billetsList.set(i + lowerValue, working.get(m2++));
                    } else if (collator.compare(name1, name2) < 0) {
                        billetsList.set(i + lowerValue, working.get(m1++));
                    } else {
                        if (date1 > date2) {
                            billetsList.set(i + lowerValue, working.get(m2++));
                        } else {
                            billetsList.set(i + lowerValue, working.get(m1++));
                        }
                    }
                } else {
                    billetsList.set(i + lowerValue, working.get(m2++));
                }
            } else {
                billetsList.set(i + lowerValue, working.get(m1++));
            }
        }
    }
