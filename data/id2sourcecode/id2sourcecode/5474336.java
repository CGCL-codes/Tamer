        @Override
        protected void compute() {
            if (rightIndex - leftIndex < indivisibleSize) {
                insertionsort();
                return;
            }
            int centerIndex = (leftIndex + rightIndex) / 2;
            invokeAll(new SortTask(leftIndex, centerIndex), new SortTask(centerIndex + 1, rightIndex));
            merge();
        }
