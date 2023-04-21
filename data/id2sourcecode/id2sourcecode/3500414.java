    private void quicksort(int leftIndex, int rightIndex) {
        int leftToRightIndex;
        int rightToLeftIndex;
        Object leftToRightValue;
        Object rightToLeftValue;
        int pivotIndex;
        Object pivotValue;
        int newLeftIndex;
        int newRightIndex;
        leftToRightIndex = leftIndex;
        rightToLeftIndex = rightIndex;
        pivotIndex = (leftToRightIndex + rightToLeftIndex) / 2;
        pivotValue = myArray[pivotIndex];
        newLeftIndex = rightIndex + 1;
        newRightIndex = leftIndex - 1;
        while ((newRightIndex + 1) < newLeftIndex) {
            leftToRightValue = myArray[leftToRightIndex];
            while (leftToRightIndex < newLeftIndex & myComp.compare(leftToRightValue, pivotValue) < 0) {
                newRightIndex = leftToRightIndex;
                leftToRightIndex++;
                leftToRightValue = myArray[leftToRightIndex];
            }
            rightToLeftValue = myArray[rightToLeftIndex];
            while (newRightIndex <= rightToLeftIndex & myComp.compare(rightToLeftValue, pivotValue) > 0) {
                newLeftIndex = rightToLeftIndex;
                rightToLeftIndex--;
                rightToLeftValue = myArray[rightToLeftIndex];
            }
            if (leftToRightIndex == rightToLeftIndex) {
                newRightIndex = leftToRightIndex;
            } else if (leftToRightIndex < rightToLeftIndex) {
                if (myComp.compare(leftToRightValue, rightToLeftValue) >= 0) {
                    Object temp = leftToRightValue;
                    myArray[leftToRightIndex] = rightToLeftValue;
                    myArray[rightToLeftIndex] = temp;
                    newLeftIndex = rightToLeftIndex;
                    newRightIndex = leftToRightIndex;
                    leftToRightIndex++;
                    rightToLeftIndex--;
                }
            }
        }
        if (leftIndex < newRightIndex) {
            quicksort(leftIndex, newRightIndex);
        }
        if (newLeftIndex < rightIndex) {
            quicksort(newLeftIndex, rightIndex);
        }
    }
