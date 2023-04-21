    public ObjectIdentifierValue getChildByValue(int value) {
        ObjectIdentifierValue child;
        int low = 0;
        int high = children.size();
        int pos;
        if (low < value && value <= high) {
            pos = value - 1;
        } else {
            pos = (low + high) / 2;
        }
        while (low < high) {
            child = (ObjectIdentifierValue) children.get(pos);
            if (child.getValue() == value) {
                return child;
            } else if (child.getValue() < value) {
                low = pos + 1;
            } else {
                high = pos;
            }
            pos = (low + high) / 2;
        }
        return null;
    }
