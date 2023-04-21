        public void mergeSort(int[] tempArrayContents, double[] tempArrayScores, int left, int right, int itemIndex) {
            if (left < right) {
                int center = (left + right) / 2;
                mergeSort(tempArrayContents, tempArrayScores, left, center, itemIndex);
                mergeSort(tempArrayContents, tempArrayScores, center + 1, right, itemIndex);
                merge(tempArrayContents, tempArrayScores, left, center + 1, right, itemIndex);
            }
        }
