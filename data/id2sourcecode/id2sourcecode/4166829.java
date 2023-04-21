    public void drawCircleCentered(short colix, int diameter, int x, int y, int z) {
        if (z < slab || z > depth) return;
        int r = (diameter + 1) / 2;
        setColix(colix);
        if ((x >= r && x + r < width) && (y >= r && y + r < height)) {
            switch(diameter) {
                case 2:
                    plotPixelUnclipped(x, y - 1, z);
                    plotPixelUnclipped(x - 1, y - 1, z);
                    plotPixelUnclipped(x - 1, y, z);
                case 1:
                    plotPixelUnclipped(x, y, z);
                case 0:
                    break;
                default:
                    circle3d.plotCircleCenteredUnclipped(x, y, z, diameter);
            }
        } else {
            switch(diameter) {
                case 2:
                    plotPixelClipped(x, y - 1, z);
                    plotPixelClipped(x - 1, y - 1, z);
                    plotPixelClipped(x - 1, y, z);
                case 1:
                    plotPixelClipped(x, y, z);
                case 0:
                    break;
                default:
                    circle3d.plotCircleCenteredClipped(x, y, z, diameter);
            }
        }
    }
