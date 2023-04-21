    private QuadTree<InundationGeometry> scaleGeometries(QuadTree<InundationGeometry> triangles, HashMap<Integer, ArrayList<InundationGeometry>> mapping) {
        QuadTree<InundationGeometry> ret = new QuadTree<InundationGeometry>(triangles.getMinEasting(), triangles.getMinNorthing(), triangles.getMaxEasting(), triangles.getMaxNorthing());
        HashSet<InundationGeometry> removed = new HashSet<InundationGeometry>();
        HashMap<Integer, ArrayList<InundationGeometry>> newMapping = new HashMap<Integer, ArrayList<InundationGeometry>>();
        int toGo = triangles.values().size();
        for (InundationGeometry p : triangles.values()) {
            if (removed.contains(p)) {
                continue;
            }
            int maxCount = p.getCoords().length - 1;
            HashSet<InundationGeometry> neighbors = new HashSet<InundationGeometry>();
            for (int i = 0; i <= maxCount; i++) {
                neighbors.addAll(mapping.get(p.getCoords()[i]));
            }
            HashSet<InundationGeometry> newNeighbors = new HashSet<InundationGeometry>();
            for (InundationGeometry ig : neighbors) {
                if (!removed.contains(ig)) {
                    newNeighbors.add(ig);
                }
            }
            if (newNeighbors.size() == 0) {
                continue;
            }
            double maxX = 0;
            double maxY = 0;
            double minX = Double.POSITIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY;
            for (InundationGeometry n : newNeighbors) {
                removed.add(n);
                maxCount = n.getCoords().length - 1;
                for (int i = 0; i <= maxCount; i++) {
                    float x = this.inundationData.xcoords[n.getCoords()[i]];
                    float y = this.inundationData.ycoords[n.getCoords()[i]];
                    maxX = maxX > x ? maxX : x;
                    minX = minX < x ? minX : x;
                    maxY = maxY > y ? maxY : y;
                    minY = minY < y ? minY : y;
                }
            }
            try {
                int c1 = this.coordinateQuadTree.get(maxX, maxY);
                int c2 = this.coordinateQuadTree.get(maxX, minY);
                int c3 = this.coordinateQuadTree.get(minX, minY);
                int c4 = this.coordinateQuadTree.get(minX, maxY);
                Quad q = new Quad(this.inundationData);
                q.coordsIdx[0] = c1;
                q.coordsIdx[1] = c2;
                q.coordsIdx[2] = c3;
                q.coordsIdx[3] = c4;
                double x = (maxX + minX) / 2;
                double y = (maxY + minY) / 2;
                ret.put(x, y, q);
                for (int i = 0; i <= 3; i++) {
                    ArrayList<InundationGeometry> tmp = newMapping.get(q.coordsIdx[i]);
                    if (tmp == null) {
                        tmp = new ArrayList<InundationGeometry>();
                        newMapping.put(q.coordsIdx[i], tmp);
                    }
                    tmp.add(q);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mapping.clear();
        mapping.putAll(newMapping);
        this.coordinateQuadTree.clear();
        for (InundationGeometry ig : ret.values()) {
            for (int i = 0; i < ig.getCoords().length; i++) {
                this.coordinateQuadTree.put(this.inundationData.xcoords[ig.getCoords()[i]], this.inundationData.ycoords[ig.getCoords()[i]], ig.getCoords()[i]);
            }
        }
        return ret;
    }
