    @Override
    public List<E> detect(AABB aabb) {
        int size = this.proxyList.size();
        List<E> list;
        if (size == 0) {
            return new ArrayList<E>();
        } else {
            list = new ArrayList<E>(size);
        }
        int index = size / 2;
        int max = size;
        int min = 0;
        while (true) {
            Proxy p = this.proxyList.get(index);
            if (p.aabb.getMinX() < aabb.getMinX()) {
                min = index;
            } else {
                max = index;
            }
            if (max - min == 1) {
                break;
            }
            index = (min + max) / 2;
        }
        for (int i = 0; i < size; i++) {
            Proxy p = this.proxyList.get(i);
            if (p.aabb.getMaxX() > aabb.getMinX()) {
                if (p.aabb.overlaps(aabb)) {
                    list.add(p.collidable);
                }
            } else {
                if (i >= index) break;
            }
        }
        return list;
    }
