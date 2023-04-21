    @SuppressWarnings("unchecked")
    private void sortNumbers(List<String> numbers, int lowerValue, int hightValue) throws Exception {
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortNumbers(numbers, lowerValue, pivot);
        sortNumbers(numbers, pivot + 1, hightValue);
        List<String> working = new ArrayList<String>();
        for (int i = 0; i < length; i++) working.add(i, numbers.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    int number1 = Integer.parseInt(working.get(m1));
                    int number2 = Integer.parseInt(working.get(m2));
                    if (number1 > number2) {
                        numbers.set(i + lowerValue, working.get(m2++));
                    } else {
                        numbers.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    numbers.set(i + lowerValue, working.get(m2++));
                }
            } else {
                numbers.set(i + lowerValue, working.get(m1++));
            }
        }
    }
