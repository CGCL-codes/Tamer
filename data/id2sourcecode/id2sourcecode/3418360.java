    private static void quicksort(ArrayList<Client> v, int firstIndex, int lastIndex) {
        String key;
        int leftPtr, rightPtr, middle;
        if (firstIndex < lastIndex) {
            middle = (firstIndex + lastIndex) / 2;
            swap(v, firstIndex, middle);
            key = v.get(firstIndex).getID();
            leftPtr = firstIndex + 1;
            rightPtr = lastIndex;
            while (leftPtr <= rightPtr) {
                while ((leftPtr <= lastIndex) && ((v.get(leftPtr)).getID().compareTo(key) <= 0)) leftPtr++;
                while ((rightPtr >= firstIndex) && ((v.get(rightPtr)).getID().compareTo(key) > 0)) rightPtr--;
                if (leftPtr < rightPtr) swap(v, leftPtr, rightPtr);
            }
            swap(v, firstIndex, rightPtr);
            quicksort(v, firstIndex, rightPtr - 1);
            quicksort(v, rightPtr + 1, lastIndex);
        }
    }
