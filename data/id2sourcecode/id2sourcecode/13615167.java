    public static final <T, S extends T, U extends T> int findPositionInOrder(List<S> collection, U value, Comparator<T> comp) {
        int position;
        if (comp.compare(value, collection.get(0)) <= 0) {
            position = 0;
        } else if (comp.compare(value, collection.get(collection.size() - 1)) > 0) {
            position = collection.size();
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
            position = b;
        }
        return position;
    }
