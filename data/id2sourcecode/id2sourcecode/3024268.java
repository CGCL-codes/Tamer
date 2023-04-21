        public void run() {
            for (int y = 0; y < getHeight(); y++) for (int x = xMin; x < xMax; x++) writeGrayData(x, y, readGray(x, y) > threshold ? 255 : 0);
        }
