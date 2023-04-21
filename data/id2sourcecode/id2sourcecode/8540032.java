        int sort(int left, int right, boolean partition) {
            if (partition) {
                int i, j, middle;
                middle = (left + right) / 2;
                int s = edges[right];
                edges[right] = edges[middle];
                edges[middle] = s;
                for (i = left - 1, j = right; ; ) {
                    while (y[edges[++i]] < y[edges[right]]) ;
                    while (j > left && y[edges[--j]] > y[edges[right]]) ;
                    if (i >= j) break;
                    s = edges[i];
                    edges[i] = edges[j];
                    edges[j] = s;
                }
                s = edges[right];
                edges[right] = edges[i];
                edges[i] = s;
                return i;
            } else {
                if (left >= right) return 0;
                int p = sort(left, right, true);
                sort(left, p - 1, false);
                sort(p + 1, right, false);
                return 0;
            }
        }
