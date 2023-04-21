    private synchronized void mergeSort(int start, int end, Obj[] tmp) {
        if (start < end) {
            int middle = (start + end) / 2;
            mergeSort(start, middle, tmp);
            mergeSort(middle + 1, end, tmp);
            int i0 = middle;
            int i1 = end;
            for (int i = end; i >= start; --i) {
                if (i0 < start) tmp[i] = (Obj) m_storage.elementAt(i1--); else if (i1 <= middle) tmp[i] = (Obj) m_storage.elementAt(i0--); else if (((Obj) m_storage.elementAt(i1)).lessThan((Obj) m_storage.elementAt(i0), sortKey)) tmp[i] = (Obj) m_storage.elementAt(i0--); else tmp[i] = (Obj) m_storage.elementAt(i1--);
            }
            for (int i = start; i <= end; ++i) {
                m_storage.setElementAt(tmp[i], i);
            }
        }
    }
