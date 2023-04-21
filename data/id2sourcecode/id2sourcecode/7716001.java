        int findInsertionPoint(long key) {
            int sz = getSize();
            int start = 0;
            int end = sz - 1;
            int mid = 0;
            long mkey = 0;
            boolean found = false;
            while ((start <= end) && (start >= 0) && (end < maxEntriesPerNode) && !found) {
                mid = (start + end) / 2;
                mkey = getKey(mid);
                if (key > mkey) start = mid + 1; else if (key < mkey) end = mid - 1; else found = true;
            }
            if (found) {
                while ((mkey == key) && (++mid < sz)) mkey = getKey(mid);
            } else {
                if ((mkey < key) && (mid < sz)) mid++;
            }
            return mid;
        }
