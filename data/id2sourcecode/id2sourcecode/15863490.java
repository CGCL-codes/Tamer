    private Position[] findPosition(int offset, int length, Position[] positions) {
        if (positions.length == 0) return null;
        int rangeEnd = offset + length;
        int left = 0;
        int right = positions.length - 1;
        int mid = 0;
        Position position = null;
        while (left < right) {
            mid = (left + right) / 2;
            position = positions[mid];
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
            position = positions[index];
            while (index >= 0 && (position.getOffset() + position.getLength()) > offset) {
                index--;
                if (index > 0) {
                    position = positions[index];
                }
            }
        }
        index++;
        position = positions[index];
        while (index < positions.length && (position.getOffset() < rangeEnd)) {
            list.add(position);
            index++;
            if (index < positions.length) {
                position = positions[index];
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        return (Position[]) list.toArray(new Position[list.size()]);
    }
