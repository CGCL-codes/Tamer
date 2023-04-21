    protected int findInsertionIndex_TypeArray(List<E> list, E element, int left, int right, ObjectPointer_1x0<Integer> rightBorder) {
        if (rightBorder == null) throw new IllegalArgumentException("rightBorder can't be null");
        if (right < left) throw new IllegalArgumentException("right must be bigger or equals as the left");
        if ((right - left) <= minListSizeForArrayType) {
            rightBorder.setObject(right);
            return findInsertionIndex_TypeLinked(list, element, left, right);
        }
        int midle = left + (right - left) / 2;
        E midleE = this.decorated.get(midle);
        int comparedValue = this.comparator.compare(midleE, element);
        if (0 < comparedValue) {
            return this.findInsertionIndex_TypeArray(list, element, left, midle, rightBorder);
        } else if (comparedValue == 0) {
            ListIterator<E> it = this.listIterator(midle);
            int index = midle;
            while (it.hasPrevious()) {
                E e = it.previous();
                if (0 != this.comparator.compare(e, element)) {
                    break;
                }
                index--;
            }
            rightBorder.setObject(index);
            return index;
        }
        return this.findInsertionIndex_TypeArray(list, element, midle, right, rightBorder);
    }
