    private void sortKey(String[] names) {
        for (int i = 0; i + 1 < names.length; i++) {
            if (names[i].compareTo(names[i + 1]) > 0) {
                String temp = names[i];
                names[i] = names[i + 1];
                names[i + 1] = temp;
                int j = i;
                boolean done = false;
                while (j != 0 && !done) {
                    if (names[j].compareTo(names[j - 1]) < 0) {
                        temp = names[j];
                        names[j] = names[j - 1];
                        names[j - 1] = temp;
                    } else done = true;
                    j--;
                }
            }
        }
    }
