    public static <T, S extends T, U extends T> S findNextInOrder(List<S> collection, U value, Comparator<T> comp) {
        S ret;
        if (comp.compare(value, collection.get(0)) <= 0) {
            ret = collection.get(0);
        } else if (comp.compare(value, collection.get(collection.size() - 1)) > 0) {
            ret = null;
        } else {
            int a = 0;
            int b = collection.size() - 1;
            int middle;
            while (b - a > 1) {
                middle = (a + b) / 2;
                if (comp.compare(value, collection.get(middle)) > 0) {
                    a = middle;
                } else {
                    b = middle;
                }
            }
            System.out.println("B = " + b);
            ret = collection.get(b);
        }
        return ret;
    }
