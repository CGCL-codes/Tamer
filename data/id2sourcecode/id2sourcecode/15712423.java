    public void makeMove() {
        if (!canMakeMove) {
            throw new IllegalStateException("No more moves can be made!");
        }
        if (elements[movingPtr] > elements[movingPtr + 1]) {
            int tmp = elements[movingPtr];
            elements[movingPtr] = elements[movingPtr + 1];
            elements[movingPtr + 1] = tmp;
            v.swap(movingPtr, movingPtr + 1);
        }
        movingPtr++;
        if (movingPtr == rightPtr) {
            movingPtr = 0;
            --rightPtr;
            if (0 == rightPtr) {
                canMakeMove = false;
            }
        }
    }
