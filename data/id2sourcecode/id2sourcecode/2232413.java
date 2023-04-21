    public BaseNode[] getPath() {
        int count = 0;
        BaseNode node = this;
        while (node != null) {
            count++;
            node = node.getParent();
        }
        BaseNode ret[] = new BaseNode[count];
        node = this;
        for (int i = count - 1; i >= 0; i--) {
            ret[i] = node;
            node = node.getParent();
        }
        return ret;
    }
