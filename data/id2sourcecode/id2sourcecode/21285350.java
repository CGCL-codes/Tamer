        public void relocate(int oldIndex, int newIndex) {
            if (oldIndex < 0 || oldIndex > tests.length - 1) {
                throw new java.lang.IllegalArgumentException();
            }
            if (newIndex < 0 || newIndex > tests.length - 1) {
                throw new java.lang.IllegalArgumentException();
            } else {
                if (newIndex > oldIndex) {
                    Test temp = tests[oldIndex];
                    for (int i = oldIndex; i < newIndex; i++) tests[i] = tests[i + 1];
                    tests[newIndex] = temp;
                } else if (oldIndex > newIndex) {
                    Test temp = tests[oldIndex];
                    for (int j = oldIndex; j > newIndex; j--) tests[j] = tests[j - 1];
                    tests[newIndex] = temp;
                }
            }
        }
