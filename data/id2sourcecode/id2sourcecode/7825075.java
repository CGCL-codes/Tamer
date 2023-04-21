        boolean contains(int i) {
            int left = 0;
            int right = this.right;
            while (left < right) {
                insertIndex = (left + right) / 2;
                if (set[insertIndex] < i) {
                    left = insertIndex + 1;
                } else {
                    right = insertIndex;
                }
            }
            insertIndex = right;
            if (right < this.right && set[right] == i) {
                return true;
            }
            return false;
        }
