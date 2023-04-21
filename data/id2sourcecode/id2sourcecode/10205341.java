    public int indexOfRetentionTime(double rt) {
        if (peaks_sorted_retentiontime == null) {
            peaks_sorted_retentiontime = (Vector<Type>) peaks.clone();
            Collections.sort(peaks_sorted_retentiontime, IPeak.sort_retentiontime_ascending);
        }
        if (peaks_sorted_retentiontime.size() <= 1) return -1;
        if (rt < peaks_sorted_retentiontime.firstElement().getRetentionTime() || rt > peaks_sorted_retentiontime.lastElement().getRetentionTime()) return -1;
        if (rt >= peaks_sorted_retentiontime.firstElement().getRetentionTime() && rt < peaks_sorted_retentiontime.get(1).getRetentionTime()) return 0;
        if (rt == peaks_sorted_retentiontime.lastElement().getRetentionTime()) return peaks_sorted_retentiontime.size() - 1;
        int index = peaks_sorted_retentiontime.size() / 2;
        int end_index = peaks_sorted_retentiontime.size() - 1;
        int begin_index = 0;
        while (true) {
            double current_rt = peaks_sorted_retentiontime.get(index).getRetentionTime();
            int new_index;
            if (current_rt > rt) {
                new_index = (begin_index + index) / 2;
                end_index = index;
            } else {
                new_index = (end_index + index) / 2;
                begin_index = index;
            }
            current_rt = peaks_sorted_retentiontime.get(new_index).getRetentionTime();
            double next_rt = peaks_sorted_retentiontime.get(new_index + 1).getRetentionTime();
            if (current_rt <= rt && next_rt > rt) return new_index; else index = new_index;
        }
    }
