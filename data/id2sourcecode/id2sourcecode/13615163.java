    public static <N extends Number, M extends Number> int findClosestIndexInOrder(List<N> collection, M value) {
        int ret;
        if (MathOperations.compare(value, collection.get(0)) <= 0) {
            ret = 0;
        } else if (MathOperations.compare(value, collection.get(collection.size() - 1)) > 0) {
            ret = collection.size() - 1;
        } else {
            int a = 0;
            int b = collection.size() - 1;
            int middle;
            while (b - a > 1) {
                middle = (a + b) / 2;
                if (MathOperations.compare(value, collection.get(middle)) > 0) {
                    a = middle;
                } else {
                    b = middle;
                }
            }
            System.out.println("B = " + b);
            double searchValue = MathOperations.extractValue(value);
            double diffLeft = Math.abs(searchValue - MathOperations.extractValue(collection.get(b - 1)));
            double diffRight = Math.abs(MathOperations.extractValue(collection.get(b)) - searchValue);
            if (diffLeft <= diffRight) {
                ret = b - 1;
            } else {
                ret = b;
            }
        }
        return ret;
    }
