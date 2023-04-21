    private void doMergeSort() {
        if ((endIndex - startIndex) < MIN_MERGE_LEN) {
            doInsertSort();
            return;
        }
        int midIndex = startIndex + (endIndex - startIndex) / 2;
        MergeSortActivity<T> leftSorter = new MergeSortActivity<T>(startIndex, midIndex, list, runtime);
        MergeSortActivity<T> rightSorter = new MergeSortActivity<T>(midIndex, endIndex, list, runtime);
        if (runtime.reserveTwoThreads()) {
            Future<?> lFuture = runtime.getExecutor().submit(leftSorter);
            Future<?> rFuture = runtime.getExecutor().submit(rightSorter);
            try {
                lFuture.get();
                runtime.releaseThread();
                rFuture.get();
                runtime.releaseThread();
            } catch (Exception e) {
                System.err.print("Sorry, error occured\n");
                e.printStackTrace();
                System.exit(-1);
            }
        } else {
            leftSorter.run();
            rightSorter.run();
        }
        int lIndex = 0;
        int rIndex = 0;
        while (lIndex != leftSorter.getResult().size() || rIndex != rightSorter.getResult().size()) {
            T nextElement = null;
            if (rIndex == rightSorter.getResult().size()) {
                nextElement = leftSorter.getResult().get(lIndex++);
            } else if (lIndex == leftSorter.getResult().size()) {
                nextElement = rightSorter.getResult().get(rIndex++);
            } else {
                if (leftSorter.getResult().get(lIndex).compareTo(rightSorter.getResult().get(rIndex)) < 0) {
                    nextElement = leftSorter.getResult().get(lIndex++);
                } else {
                    nextElement = rightSorter.getResult().get(rIndex++);
                }
            }
            sortedList.add(nextElement);
        }
        return;
    }
