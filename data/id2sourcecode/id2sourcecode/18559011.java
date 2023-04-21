    public int getFirstIndexBySize(long size) {
        int left = 0;
        int right = items.size() - 1;
        while (left <= right) {
            int center = (left + right) / 2;
            if (items.get(center).getSize() == size) {
                while (center > 0 && items.get(center - 1).getSize() == size) {
                    center--;
                }
                return center;
            } else if (items.get(center).getSize() > size) {
                left = center + 1;
            } else {
                right = center - 1;
            }
        }
        return -1;
    }
