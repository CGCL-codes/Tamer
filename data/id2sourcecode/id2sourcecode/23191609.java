    protected static Sortable median3(Vector v, int l, int r) {
        int center = (l + r) / 2;
        Object temp;
        if (((Sortable) v.elementAt(l)).greaterThan(((Sortable) v.elementAt(center))) > 0) {
            temp = v.elementAt(l);
            v.setElementAt(v.elementAt(center), l);
            v.setElementAt(temp, center);
        }
        if (((Sortable) v.elementAt(l)).greaterThan(((Sortable) v.elementAt(r))) > 0) {
            temp = v.elementAt(l);
            v.setElementAt(v.elementAt(r), l);
            v.setElementAt(temp, r);
        }
        if (((Sortable) v.elementAt(center)).greaterThan(((Sortable) v.elementAt(r))) > 0) {
            temp = v.elementAt(center);
            v.setElementAt(v.elementAt(r), center);
            v.setElementAt(temp, r);
        }
        temp = v.elementAt(center);
        v.setElementAt(v.elementAt(r - 1), center);
        v.setElementAt(temp, r - 1);
        return ((Sortable) v.elementAt(r - 1));
    }
