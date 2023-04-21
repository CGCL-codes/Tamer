    public int indexOfScan(int scanid) {
        if (peaks_sorted_scanid == null) {
            peaks_sorted_scanid = (Vector<Type>) peaks.clone();
            Collections.sort(peaks_sorted_scanid, IPeak.sort_scanid_ascending);
        }
        if (peaks_sorted_scanid.size() == 0) return -1;
        if (scanid < peaks_sorted_scanid.firstElement().getScanID() || scanid > peaks_sorted_scanid.lastElement().getScanID()) return -1;
        if (scanid >= peaks_sorted_scanid.firstElement().getScanID() && scanid < peaks_sorted_scanid.get(1).getScanID()) return 0;
        if (scanid == peaks_sorted_scanid.lastElement().getScanID()) return peaks_sorted_scanid.size() - 1;
        int index = peaks_sorted_scanid.size() / 2;
        int end_index = peaks_sorted_scanid.size() - 1;
        int begin_index = 0;
        while (true) {
            double current_scanid = peaks_sorted_scanid.get(index).getScanID();
            int new_index;
            if (current_scanid > scanid) {
                new_index = (begin_index + index) / 2;
                end_index = index;
            } else {
                new_index = (end_index + index) / 2;
                begin_index = index;
            }
            current_scanid = peaks_sorted_scanid.get(new_index).getScanID();
            double next_scanid = peaks_sorted_scanid.get(new_index + 1).getScanID();
            if (current_scanid <= scanid && next_scanid > scanid) return new_index; else index = new_index;
        }
    }
