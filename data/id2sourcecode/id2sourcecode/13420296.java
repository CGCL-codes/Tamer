        private int getline(int addrq) {
            int lo = 0;
            int hi = addr_breakpoints.size();
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (addrq < addr_breakpoints.get(mid)) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            return lines.get(lo);
        }
