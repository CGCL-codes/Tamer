        public Rect(final double minX, final double minY, final double maxX, final double maxY) {
            this.minX = Math.min(minX, maxX);
            this.minY = Math.min(minY, maxY);
            this.maxX = Math.max(minX, maxX);
            this.maxY = Math.max(minY, maxY);
            this.centerX = (minX + maxX) / 2;
            this.centerY = (minY + maxY) / 2;
        }
