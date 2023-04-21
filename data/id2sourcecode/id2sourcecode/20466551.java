    protected void binaryInsertion(List newSortedItems) {
        int lo = 0;
        int hi = m_sortedItems.size();
        for (int i = 0; i < newSortedItems.size(); i++) {
            RDFNode newItem = (RDFNode) newSortedItems.get(i);
            while (lo < hi) {
                int middle = (lo + hi) / 2;
                RDFNode itemMiddle = (RDFNode) m_sortedItems.get(middle);
                int compare = compareItems(newItem, itemMiddle);
                if (compare < 0) {
                    hi = middle;
                } else {
                    lo = middle + 1;
                }
            }
            m_sortedItems.add(lo, newItem);
            notifyDataConsumers(DataConstants.LIST_ADDITION, new ListDataChange(lo, 1, null));
            hi = m_sortedItems.size() - 1;
        }
    }
