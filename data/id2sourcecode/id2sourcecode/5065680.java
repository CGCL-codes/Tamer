    @Override
    protected void computeDirectionArrows(DrawContext dc, PathData pathData) {
        IntBuffer polePositions = pathData.getPolePositions();
        int numPositions = polePositions.limit() / 2;
        List<Position> tessellatedPositions = pathData.getTessellatedPositions();
        final int FLOATS_PER_ARROWHEAD = 9;
        FloatBuffer buffer = (FloatBuffer) pathData.getValue(ARROWS_KEY);
        if (buffer == null || buffer.capacity() < numPositions * FLOATS_PER_ARROWHEAD) buffer = BufferUtil.newFloatBuffer(FLOATS_PER_ARROWHEAD * numPositions);
        pathData.setValue(ARROWS_KEY, buffer);
        buffer.clear();
        Terrain terrain = dc.getTerrain();
        double arrowBase = this.getArrowLength() * this.getArrowAngle().tanHalfAngle();
        int thisPole = polePositions.get(0) / 2;
        Position poleA = tessellatedPositions.get(thisPole);
        Vec4 polePtA = this.computePoint(terrain, poleA);
        int num = 0;
        for (int i = 2; i < polePositions.limit(); i += 2) {
            int nextPole = polePositions.get(i) / 2;
            Position poleB = tessellatedPositions.get(nextPole);
            Vec4 polePtB = this.computePoint(terrain, poleB);
            int midPoint = (thisPole + nextPole) / 2;
            Position posA = tessellatedPositions.get(midPoint);
            Position posB = tessellatedPositions.get(midPoint + 1);
            Vec4 ptA = this.computePoint(terrain, posA);
            Vec4 ptB = this.computePoint(terrain, posB);
            if (this.directions != null) {
                if (this.directions.get(num) != LEG_AUTHORIZED && this.directions.get(num) != LEG_FORBIDDEN) {
                    this.computeArrowheadGeometry(dc, polePtA, polePtB, ptA, ptB, this.getArrowLength(), arrowBase, buffer, pathData, this.directions.get(num));
                }
            }
            thisPole = nextPole;
            polePtA = polePtB;
            num++;
        }
    }
