        private boolean add(int m, Object[] ret, int r) throws E {
            entriesSet[m] = true;
            T1 item = dl.added(modifier[m], m, r);
            if (isNullElement) {
                item = (T1) NULL;
                isNullElement = false;
            }
            if (item != null) {
                ret[r] = item;
                for (int i = 0; i < oIdxAdj.length; i++) if (oIdxAdj[i] >= r) oIdxAdj[i]++;
            } else {
                for (; r < ret.length - 1; r++) ret[r] = ret[r + 1];
            }
            return item != null;
        }
