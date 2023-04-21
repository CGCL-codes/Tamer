    public int search(Date date) throws NotRecordedDate {
        int p_index;
        int min = 0;
        int max = dates.size() - 1;
        if (max < min) throw new NotRecordedDate(date, 0);
        do {
            p_index = (max + min) / 2;
            if (((Date) (dates.get(p_index))).Equal_To(date)) {
                return p_index;
            }
            if (min == max) {
                throw new NotRecordedDate(date, min + 1);
            }
            if (((Date) (dates.get(p_index))).Smaller_Than(date)) {
                min = p_index + 1;
            } else {
                max = p_index;
            }
        } while (true);
    }
