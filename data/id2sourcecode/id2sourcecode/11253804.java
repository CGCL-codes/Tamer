    public static String get_gene_name_from_map(GeneMap[] a, int x) {
        int low = 0;
        int high = a.length - 1;
        if (a[low].get_end() >= x) {
            return a[low].get_Accesssion();
        }
        while (low < high) {
            int mid = (low + high) / 2;
            if (a[mid - 1].get_end() < x && a[mid].get_end() >= x) {
                return a[mid].get_Accesssion();
            }
            if (a[mid].get_end() < x) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return "broken algorithm!";
    }
