    public static Object getGlobal(Vector list, String ID) {
        if (list.size() == 0) return null;
        int start = 0;
        int end = list.size() - 1;
        while (start <= end) {
            int mid = (end + start) / 2;
            int comp = classID(list.elementAt(mid)).compareToIgnoreCase(ID);
            if (comp == 0) return list.elementAt(mid); else if (comp > 0) end = mid - 1; else start = mid + 1;
        }
        return null;
    }
