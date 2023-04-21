    private void determineCenter() {
        translateToGUIPosition(0, 0, 0);
        int minX = Math.max(guiX, 0), minY = Math.max(guiY, 0);
        translateToGUIPosition(Configuration.dimX, Configuration.dimY, Configuration.dimZ);
        int maxX = Math.min(guiX, width), maxY = Math.min(guiY, height);
        centerX = (minX + maxX) / 2;
        centerY = (minY + maxY) / 2;
    }
