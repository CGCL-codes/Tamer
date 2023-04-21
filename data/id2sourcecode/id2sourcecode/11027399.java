    final void buildOctants(int maxDepth, int maxItems, ArrayList triangleList, int depth, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (triangleList.size() == 0) {
            this.isLeaf = true;
        } else if ((depth > maxDepth) || (triangleList.size() < maxItems)) {
            this.isLeaf = true;
            child = new RtTriangle[triangleList.size()];
            for (int i = 0; i < triangleList.size(); i++) {
                child[i] = (RtTriangle) triangleList.get(i);
            }
        } else {
            double midX = (minX + maxX) / 2;
            double midY = (minY + maxY) / 2;
            double midZ = (minZ + maxZ) / 2;
            isLeaf = false;
            this.octant = new RtOctTree[8];
            octant[0] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, minX, minY, minZ, midX, midY, midZ);
            octant[1] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, midX, minY, minZ, maxX, midY, midZ);
            octant[2] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, minX, minY, midZ, midX, midY, maxZ);
            octant[3] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, midX, minY, midZ, maxX, midY, maxZ);
            octant[4] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, minX, midY, minZ, midX, maxY, midZ);
            octant[5] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, midX, midY, minZ, maxX, maxY, midZ);
            octant[6] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, minX, midY, midZ, midX, maxY, maxZ);
            octant[7] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, midX, midY, midZ, maxX, maxY, maxZ);
            for (int i = 0; i < 8; i++) {
                if ((octant[i].isLeaf) && (octant[i].child == null)) {
                    octant[i] = null;
                }
            }
        }
    }
