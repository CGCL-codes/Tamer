        public E poll() {
            if (isEmpty()) {
                return null;
            }
            E e = (E) elements[0];
            for (int i = 0; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
            size--;
            return e;
        }
