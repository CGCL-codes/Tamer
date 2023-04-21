    public TreeMap refineQuery(String wrd, TreeMap foundList) {
        if (m_indexReady == false) return null;
        if (foundList == null) return null;
        TreeMap tree = find(wrd);
        if (tree == null) return null;
        TreeMap queryTree = null;
        StringEntry strEntry;
        String s;
        Object obj;
        while ((tree.size() > 0) && (foundList.size() > 0)) {
            obj = tree.firstKey();
            strEntry = (StringEntry) foundList.remove(obj);
            if (strEntry != null) {
                if (queryTree == null) queryTree = new TreeMap();
                s = String.valueOf(strEntry.digest());
                queryTree.put(s, strEntry);
            }
            tree.remove(obj);
        }
        return queryTree;
    }
