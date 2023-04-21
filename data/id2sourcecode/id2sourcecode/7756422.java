    static void quickSort(Vector vect, int el, int yu) {
        if (yu - el < 16) {
            return;
        }
        int m, pivot, other;
        m = (yu + el) / 2;
        if (((Value) vect.elementAt(el)).compareTo(vect.elementAt(yu)) < 0) {
            pivot = el;
            other = yu;
        } else {
            pivot = yu;
            other = el;
        }
        if (((Value) vect.elementAt(pivot)).compareTo(vect.elementAt(m)) < 0) {
            if (((Value) vect.elementAt(m)).compareTo(vect.elementAt(other)) < 0) {
                pivot = m;
            } else {
                pivot = other;
            }
        }
        swap(vect, el, pivot);
        int i, j;
        i = el + 1;
        j = yu - 1;
        while (true) {
            while (((Value) vect.elementAt(el)).compareTo(vect.elementAt(i)) < 0) {
                i++;
            }
            while (((Value) vect.elementAt(el)).compareTo(vect.elementAt(j)) > 0) {
                j--;
            }
            if (i >= j) {
                break;
            }
            swap(vect, i, j);
            i++;
            j--;
        }
        swap(vect, el, j);
        if (j - el < yu - i) {
            quickSort(vect, el, j - 1);
            quickSort(vect, i, yu);
        } else {
            quickSort(vect, i, yu);
            quickSort(vect, el, j - 1);
        }
    }
