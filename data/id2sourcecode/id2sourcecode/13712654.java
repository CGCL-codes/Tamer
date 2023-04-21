    public ITypedRegion[] computePartitioning(int offset, int length) {
        int rangeEnd = offset + length;
        int left = 0;
        int right = partitions.size() - 1;
        int mid = 0;
        IOConsolePartition position = null;
        if (left == right) {
            return new IOConsolePartition[] { (IOConsolePartition) partitions.get(0) };
        }
        while (left < right) {
            mid = (left + right) / 2;
            position = (IOConsolePartition) partitions.get(mid);
            if (rangeEnd < position.getOffset()) {
                if (left == mid) right = left; else right = mid - 1;
            } else if (offset > (position.getOffset() + position.getLength() - 1)) {
                if (right == mid) left = right; else left = mid + 1;
            } else {
                left = right = mid;
            }
        }
        List list = new ArrayList();
        int index = left - 1;
        if (index >= 0) {
            position = (IOConsolePartition) partitions.get(index);
            while (index >= 0 && (position.getOffset() + position.getLength()) > offset) {
                index--;
                if (index >= 0) {
                    position = (IOConsolePartition) partitions.get(index);
                }
            }
        }
        index++;
        position = (IOConsolePartition) partitions.get(index);
        while (index < partitions.size() && (position.getOffset() < rangeEnd)) {
            list.add(position);
            index++;
            if (index < partitions.size()) {
                position = (IOConsolePartition) partitions.get(index);
            }
        }
        return (ITypedRegion[]) list.toArray(new IOConsolePartition[list.size()]);
    }
