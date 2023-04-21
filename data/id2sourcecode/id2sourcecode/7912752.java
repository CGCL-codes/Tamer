        @Override
        public void run() {
            if (leftIndex == rightIndex) return;
            if (rightIndex - leftIndex > indivisibleSize) {
                try {
                    int centerIndex = (leftIndex + rightIndex) / 2;
                    Future<?> task1 = executor.submit(new SortTask(leftIndex, centerIndex));
                    Future<?> task2 = executor.submit(new SortTask(centerIndex + 1, rightIndex));
                    task1.get();
                    task2.get();
                    merge();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                insertionsort();
            }
        }
