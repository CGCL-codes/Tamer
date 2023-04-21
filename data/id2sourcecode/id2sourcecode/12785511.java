    void RecQuickSort(int nFirst, int nLast) {
        int pivot, front, back;
        if (nFirst < nLast) {
            if (nFirst < nLast - 5) {
                int center = (nFirst + nLast) / 2;
                if (m_theList.GetElem(center).GreaterThan(m_theList.GetElem(nFirst), m_theList)) m_theList.Swap(center, nFirst);
                if (m_theList.GetElem(center).GreaterThan(m_theList.GetElem(nLast), m_theList)) m_theList.Swap(center, nLast);
                if (m_theList.GetElem(nFirst).GreaterThan(m_theList.GetElem(nLast), m_theList)) m_theList.Swap(nFirst, nLast);
            }
            pivot = nFirst;
            front = nFirst;
            back = nLast + 1;
            for (; ; ) {
                do {
                    front++;
                } while (m_theList.GetElem(front).LessThan(m_theList.GetElem(pivot), m_theList) && front < nLast);
                do {
                    back--;
                } while (m_theList.GetElem(back).GreaterThan(m_theList.GetElem(pivot), m_theList));
                if (front < back) m_theList.Swap(front, back); else break;
                if (m_sorterAlgorithm.m_parent.do_abort_sort) return;
            }
            m_theList.Swap(pivot, back);
            if (m_sorterAlgorithm.m_parent.do_abort_sort) return;
            RecQuickSort(nFirst, back - 1);
            if (m_sorterAlgorithm.m_parent.do_abort_sort) return;
            RecQuickSort(back + 1, nLast);
            if (m_sorterAlgorithm.m_parent.do_abort_sort) return;
        }
    }
