        public void removeElementAt(int i) {
            if (i >= size || i < 0) throw new RuntimeException("tried to remove an element outside the vector's limits");
            for (int j = i; j < size - 1; j++) store[j] = store[j + 1];
            setSize(size - 1);
        }
