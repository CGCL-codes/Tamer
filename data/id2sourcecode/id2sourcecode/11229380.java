        NodeProxy get(NodeId nodeId) {
            int low = 0;
            int high = length - 1;
            int mid, cmp;
            NodeProxy p;
            while (low <= high) {
                mid = (low + high) / 2;
                p = array[mid];
                cmp = p.getNodeId().compareTo(nodeId);
                if (cmp == 0) {
                    return p;
                }
                if (cmp > 0) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
            return null;
        }
