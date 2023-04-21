        public Iterator<E> iterator() {
            return new Iterator<E>() {

                private int currentIndex = -1;

                public boolean hasNext() {
                    return size > 0 && currentIndex < size;
                }

                public E next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    currentIndex++;
                    return (E) elements[currentIndex];
                }

                public void remove() {
                    if (-1 == currentIndex) {
                        throw new IllegalStateException();
                    }
                    for (int i = currentIndex; i < size - 1; i++) {
                        elements[i] = elements[i + 1];
                    }
                    size--;
                }
            };
        }
