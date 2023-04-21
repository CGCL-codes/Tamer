        public static int randomizedPartition(int[] array, int startIndex, int endIndex) {
            int i = startIndex + random.nextInt(endIndex - startIndex);
            swap(array, i, startIndex);
            return partition(array, startIndex, endIndex);
        }
