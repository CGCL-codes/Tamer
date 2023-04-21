        public void setPosition(NodeProxy node) {
            currentPart = getPart(node.getDocument(), false, -1);
            int low = 0;
            int high = currentPart.length - 1;
            int mid;
            NodeProxy p;
            while (low <= high) {
                mid = (low + high) / 2;
                p = currentPart.array[mid];
                int cmp = p.getNodeId().compareTo(node.getNodeId());
                if (cmp == 0) {
                    pos = mid;
                    next = p;
                    return;
                }
                if (cmp > 0) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
            next = null;
        }
