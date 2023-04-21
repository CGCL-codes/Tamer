        TreeCell(int depth, float minX, float maxX, float minY, float maxY) {
            this.depth = (byte) depth;
            this.minRangeX = minX;
            this.maxRangeX = maxX;
            this.minRangeY = minY;
            this.maxRangeY = maxY;
            this.midRangeX = (minX + maxX) / 2;
            this.midRangeY = (minY + maxY) / 2;
            if (depth == maxDepth) numberSmallCells++;
        }
