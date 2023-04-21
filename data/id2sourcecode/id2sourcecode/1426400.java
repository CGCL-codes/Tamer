        private boolean set(int o, int m, Object[] ret, int r) throws E {
            if (entriesSet[m]) return ret[r] != null;
            if (oIdxAdj[o] > r && oIdxAdj[o] - r <= 10) {
                int o2;
                for (o2 = o - 1; o2 >= 0; o2--) if (oMappings[o2] > m) set(o2, oMappings[o2], ret, r + oMappings[o2] - m);
            }
            entriesSet[m] = true;
            T1 item = dl.set(original[o], o, oIdxAdj[o], modifier[m], m, r);
            if (isNullElement) {
                item = (T1) NULL;
                isNullElement = false;
            }
            if (item != null) {
                ret[r] = item;
                int oAdj = oIdxAdj[o];
                if (r > oAdj) {
                    for (int i = 0; i < oIdxAdj.length; i++) if (oIdxAdj[i] >= oAdj && oIdxAdj[i] <= r) oIdxAdj[i]--;
                } else if (r < oAdj) {
                    for (int i = 0; i < oIdxAdj.length; i++) if (oIdxAdj[i] >= r && oIdxAdj[i] < oAdj) oIdxAdj[i]++;
                }
            } else {
                oIdxAdj[o] = -1;
                for (int i = 0; i < oIdxAdj.length; i++) if (oIdxAdj[i] > r) oIdxAdj[i]--;
                for (; r < ret.length - 1; r++) ret[r] = ret[r + 1];
            }
            return item != null;
        }
