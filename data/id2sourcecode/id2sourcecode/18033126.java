    private Position findPosition(int offset, Position[] positions) {
        if (positions.length == 0) return null;
        int left = 0;
        int right = positions.length - 1;
        int mid = 0;
        Position position = null;
        while (left < right) {
            mid = (left + right) / 2;
            position = positions[mid];
            if (offset < position.getOffset()) {
                if (left == mid) right = left; else right = mid - 1;
            } else if (offset > (position.getOffset() + position.getLength() - 1)) {
                if (right == mid) left = right; else left = mid + 1;
            } else {
                left = right = mid;
            }
        }
        position = positions[left];
        if (offset >= position.getOffset() && (offset < (position.getOffset() + position.getLength()))) {
            return position;
        }
        return null;
    }
