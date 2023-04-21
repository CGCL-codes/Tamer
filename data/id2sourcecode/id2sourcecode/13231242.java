    @Test
    public void testTileWriting() {
        System.out.println("   read/write with individual tiles");
        int numBands = image.getNumBands();
        int[] data = new int[numBands];
        for (int i = 0; i < numBands; i++) {
            data[i] = i + 1;
        }
        for (int y = image.getMinTileY(); y < image.getMaxTileY(); y++) {
            int py = TILE_WIDTH * y;
            for (int x = image.getMinTileX(); x < image.getMaxTileX(); x++) {
                int px = TILE_WIDTH * x;
                WritableRaster tile = image.getWritableTile(x, y);
                tile.setPixel(px, py, data);
                image.releaseWritableTile(x, y);
            }
        }
        int[] tileData = new int[numBands];
        for (int y = image.getMinTileY(); y < image.getMaxTileY(); y++) {
            int py = TILE_WIDTH * y;
            for (int x = image.getMinTileX(); x < image.getMaxTileX(); x++) {
                int px = TILE_WIDTH * x;
                Raster tile = image.getTile(x, y);
                tile.getPixel(px, py, tileData);
                for (int i = 0; i < numBands; i++) {
                    assertTrue(tileData[i] == data[i]);
                }
            }
        }
    }
