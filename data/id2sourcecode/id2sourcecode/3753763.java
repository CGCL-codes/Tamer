    private static Object[] mergeSort(Order order, Object[] objects, int startIndex, int endIndex) {
        if (startIndex > endIndex) return null; else if (startIndex == endIndex) return new Object[] { objects[startIndex] }; else {
            int midPoint = startIndex + (endIndex - startIndex) / 2;
            return merge(order, mergeSort(order, objects, startIndex, midPoint), mergeSort(order, objects, midPoint + 1, endIndex));
        }
    }
