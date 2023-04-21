        private void deleteItem(int i) {
            while (++i < n) {
                key[i - 1] = key[i];
                branch[i] = branch[i + 1];
            }
            branch[n] = null;
            undersize = (--n < M);
        }
