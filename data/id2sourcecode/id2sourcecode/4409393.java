        public static <T> int randomizedPartition(List<T> array, int startIndex, int endIndex, Comparator<T> comparator) {
            int i = startIndex + random.nextInt(endIndex - startIndex);
            swap(array, i, startIndex);
            return partition(array, startIndex, endIndex, comparator);
        }
