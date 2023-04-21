    public boolean add(int value) {
        hashCode = -1;
        if (used == 0) {
            ensureCapacity(1);
            startPoints[used - 1] = value;
            endPoints[used - 1] = value;
            size++;
            return true;
        }
        if (value > endPoints[used - 1]) {
            if (value == endPoints[used - 1] + 1) {
                endPoints[used - 1]++;
            } else {
                ensureCapacity(used + 1);
                startPoints[used - 1] = value;
                endPoints[used - 1] = value;
            }
            size++;
            return true;
        }
        if (value < startPoints[0]) {
            if (value == startPoints[0] - 1) {
                startPoints[0]--;
            } else {
                ensureCapacity(used + 1);
                System.arraycopy(startPoints, 0, startPoints, 1, used - 1);
                System.arraycopy(endPoints, 0, endPoints, 1, used - 1);
                startPoints[0] = value;
                endPoints[0] = value;
            }
            size++;
            return true;
        }
        int i = 0;
        int j = used;
        do {
            int mid = i + (j - i) / 2;
            if (endPoints[mid] < value) {
                i = Math.max(mid, i + 1);
            } else if (startPoints[mid] > value) {
                j = Math.min(mid, j - 1);
            } else {
                return false;
            }
        } while (i != j);
        if (i > 0 && endPoints[i - 1] + 1 == value) {
            i--;
        } else if (i < used - 1 && startPoints[i + 1] - 1 == value) {
            i++;
        }
        if (endPoints[i] + 1 == value) {
            if (value == startPoints[i + 1] - 1) {
                endPoints[i] = endPoints[i + 1];
                System.arraycopy(startPoints, i + 2, startPoints, i + 1, used - i - 2);
                System.arraycopy(endPoints, i + 2, endPoints, i + 1, used - i - 2);
                used--;
            } else {
                endPoints[i]++;
            }
            size++;
            return true;
        } else if (startPoints[i] - 1 == value) {
            if (value == endPoints[i - 1] + 1) {
                endPoints[i - 1] = endPoints[i];
                System.arraycopy(startPoints, i + 1, startPoints, i, used - i - 1);
                System.arraycopy(endPoints, i + 1, endPoints, i, used - i - 1);
                used--;
            } else {
                startPoints[i]--;
            }
            size++;
            return true;
        } else {
            if (value > endPoints[i]) {
                i++;
            }
            ensureCapacity(used + 1);
            try {
                System.arraycopy(startPoints, i, startPoints, i + 1, used - i - 1);
                System.arraycopy(endPoints, i, endPoints, i + 1, used - i - 1);
            } catch (Exception err) {
                err.printStackTrace();
            }
            startPoints[i] = value;
            endPoints[i] = value;
            size++;
            return true;
        }
    }
