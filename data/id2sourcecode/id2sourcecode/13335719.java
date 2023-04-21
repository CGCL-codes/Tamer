                public void remove() {
                    if (-1 == currentIndex) {
                        throw new IllegalStateException();
                    }
                    for (int i = currentIndex; i < size - 1; i++) {
                        elements[i] = elements[i + 1];
                    }
                    size--;
                }
