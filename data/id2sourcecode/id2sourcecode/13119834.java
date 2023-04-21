    private void sortBirthDateByPerson(List<Person> persons, int lowerValue, int hightValue) throws Exception {
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortBirthDateByPerson(persons, lowerValue, pivot);
        sortBirthDateByPerson(persons, pivot + 1, hightValue);
        List<Person> working = new ArrayList<Person>();
        for (int i = 0; i < length; i++) {
            working.add(i, persons.get(lowerValue + i));
        }
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    if (Integer.parseInt(working.get(m1).getBirthDate().split("/")[0]) > Integer.parseInt(working.get(m2).getBirthDate().split("/")[0])) {
                        persons.set(i + lowerValue, working.get(m2++));
                    } else {
                        persons.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    persons.set(i + lowerValue, working.get(m2++));
                }
            } else {
                persons.set(i + lowerValue, working.get(m1++));
            }
        }
    }
