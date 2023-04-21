    private int getPosition(Node node, Node parentNode, int down, int up) {
        if ((up - down) == 1) {
            return up;
        } else {
            int middle = (up + down) / 2;
            int k = node.compareTo((Node) parentNode.getChildAt(middle));
            if (k == 0) {
                return middle + 1;
            } else if (k < 0) {
                return getPosition(node, parentNode, down, middle);
            } else {
                return getPosition(node, parentNode, middle, up);
            }
        }
    }
