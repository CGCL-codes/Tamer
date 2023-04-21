        public void shuttlesort(int from[], int to[], int low, int high) {
            if (high - low < 2) {
                return;
            }
            int middle = (low + high) / 2;
            shuttlesort(to, from, low, middle);
            shuttlesort(to, from, middle, high);
            int p = low;
            int q = middle;
            for (int i = low; i < high; i++) {
                if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                    to[i] = from[p++];
                } else {
                    to[i] = from[q++];
                }
            }
        }
