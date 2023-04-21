        void deleteKey(int index) {
            for (int i = index; i < numKeys; i++) {
                key[i] = key[i + 1];
                value[i] = value[i + 1];
                child[i + 1] = child[i + 2];
            }
            numKeys--;
        }
