    @SuppressWarnings("unchecked")
    void insertAlph(@SuppressWarnings("rawtypes") List v) {
        int left = 0;
        int right = v.size();
        while (left != right) {
            int middle = (left + right) / 2;
            int c = name.compareTo(((Entity) v.get(middle)).name);
            if (c < 0) right = middle; else left = middle + 1;
        }
        v.add(left, this);
    }
