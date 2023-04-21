    public static void quickSort(Sortable list, int direction) {
        if (measureTimes) readStart = System.currentTimeMillis();
        Vector elements = list.getVector();
        elements = quickSort(elements, direction);
        if (measureTimes) writeStart = System.currentTimeMillis();
        list.setVector(elements);
        if (measureTimes) endTime = System.currentTimeMillis();
        if (debug && measureTimes) {
            Debug.println(Debug.BIGINFO, "Sorter", "Additional time for sorting Sortable: " + "scan " + (initStart - readStart) + " ms, " + "write " + (endTime - writeStart) + " ms.");
        }
    }
