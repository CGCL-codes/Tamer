        private <A> void moveItem(A[] list, int oldIndex, int newIndex) {
            A item = list[oldIndex];
            if (newIndex > oldIndex) {
                for (int i = oldIndex; i < newIndex; i++) {
                    list[i] = list[i + 1];
                }
            } else {
                for (int i = oldIndex; i > newIndex; i--) {
                    list[i] = list[i - 1];
                }
            }
            list[newIndex] = item;
        }
