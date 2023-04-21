    private void quickSort(int left, int right) {
        int originalLeft = left;
        int originalRight = right;
        int midIndex = left + (right - left) / 2;
        String midToString = this.sortedStrings[midIndex];
        do {
            while (compare(this.sortedStrings[left], midToString)) left++;
            while (compare(midToString, this.sortedStrings[right])) right--;
            if (left <= right) {
                Object tmp = this.sortedObjects[left];
                this.sortedObjects[left] = this.sortedObjects[right];
                this.sortedObjects[right] = tmp;
                String tmpToString = this.sortedStrings[left];
                this.sortedStrings[left] = this.sortedStrings[right];
                this.sortedStrings[right] = tmpToString;
                left++;
                right--;
            }
        } while (left <= right);
        if (originalLeft < right) quickSort(originalLeft, right);
        if (left < originalRight) quickSort(left, originalRight);
    }
