    private int getInsertPosition(List<?> entities, MapEntity entity) {
        int pos1 = 0;
        int pos2 = entities.size();
        double newDistance = getDistKM(entity);
        while (pos1 < pos2) {
            int pos3 = (pos1 + pos2) / 2;
            double dist3 = getDistKM((MapEntity) entities.get(pos3));
            if (newDistance < dist3) pos2 = pos3; else if (newDistance > dist3) pos1 = pos3 + 1; else pos1 = pos2 = pos3;
        }
        if (pos1 < entities.size()) {
            for (int i = pos1; i >= 0 && getDistKM((MapEntity) entities.get(i)) == newDistance; i--) if (entities.get(i) == entity) return -1;
            for (int i = pos1 + 1; i < entities.size() && getDistKM((MapEntity) entities.get(i)) == newDistance; i++) if (entities.get(i) == entity) return -1;
        }
        return pos1;
    }
