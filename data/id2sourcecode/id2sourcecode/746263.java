    public long medianOf3(int left, int right) {
        int center = (left + right) / 2;
        if (theArray[left] > theArray[center]) swap(left, center);
        if (theArray[left] > theArray[right]) swap(left, right);
        if (theArray[center] > theArray[right]) swap(center, right);
        swap(center, right - 1);
        return theArray[right - 1];
    }
