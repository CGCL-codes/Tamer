        private void findBlockCenter() {
            float minAlt = Float.MAX_VALUE;
            float maxAlt = -minAlt;
            int endY = width + offsetY;
            int endX = width + offsetX;
            for (int i = offsetY; i < endY; ++i) {
                for (int j = offsetX; j < endX; ++j) {
                    float alt = gHeightMap.get(j, i);
                    if (alt <= gUndefinedElev) alt = 2 * gUndefinedElev - alt;
                    minAlt = Math.min(minAlt, alt);
                    maxAlt = Math.max(maxAlt, alt);
                }
            }
            cy = (minAlt + maxAlt) / 2;
            float wo2 = width * gGridSpacing / 2;
            cx = offsetX * gGridSpacing + wo2;
            cz = offsetY * gGridSpacing + wo2;
        }
