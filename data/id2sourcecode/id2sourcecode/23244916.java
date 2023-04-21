    @Override
    public String lockTables(String[] write_table, String[] read_table) throws Exception {
        String wt = "";
        if (write_table != null) {
            for (int i = 0; i < write_table.length; i++) {
                String tn = write_table[i].trim();
                if (tn.length() == 0) {
                    continue;
                }
                if (i > 0) {
                    wt += ", ";
                }
                wt += tn + " WRITE";
            }
        }
        String rt = "";
        if (read_table != null) {
            for (int i = 0; i < read_table.length; i++) {
                String tn = read_table[i].trim();
                if (tn.length() == 0) {
                    continue;
                }
                if (i > 0) {
                    rt += ", ";
                }
                rt += tn + " READ";
            }
        }
        return "";
    }
