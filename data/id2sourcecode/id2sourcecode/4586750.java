    private void insertSortedElement(Vector v, Element elem) {
        int low = 0;
        int high = v.size();
        while (low < high) {
            int midle = (low + high) / 2;
            Element midElem = (Element) v.elementAt(midle);
            if (elem.startOffset < midElem.startOffset) {
                high = midle;
            } else {
                low = midle + 1;
            }
        }
        v.insertElementAt(elem, low);
    }
