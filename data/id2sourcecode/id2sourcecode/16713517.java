        public int getChannelCount() {
            if (isPaletteIndexed()) {
                return 3;
            }
            int count = isGrayscale() ? 1 : 3;
            if (hasAlphaChannel()) {
                ++count;
            }
            return count;
        }
