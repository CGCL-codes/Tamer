    @Override
    public String lockTables(String[] write_table, String[] read_table) {
        String wt = Merger.getFilteredAndMergedArray(write_table);
        String rt = Merger.getFilteredAndMergedArray(read_table);
        if (wt.length() > 0 && rt.length() > 0) {
            return "";
        } else if (wt.length() > 0 && rt.length() == 0) {
            return "";
        } else if (wt.length() == 0 && rt.length() > 0) {
            return "";
        } else {
            return "";
        }
    }
