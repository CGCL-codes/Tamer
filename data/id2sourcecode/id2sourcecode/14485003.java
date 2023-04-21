        private ZoomInfo createRandomZoomInfo() {
            int angle = MIN_ROTATION + RANDOM.nextInt(MAX_ROTATION - MIN_ROTATION);
            if (RANDOM.nextBoolean()) {
                angle = -angle;
            }
            final float offsetX = MIN_OFFSET + RANDOM.nextFloat() * (MAX_OFFSET - MIN_OFFSET);
            final float offsetY = MIN_OFFSET + RANDOM.nextFloat() * (MAX_OFFSET - MIN_OFFSET);
            int driftAngle = (int) (Math.atan2(offsetY, offsetX) * 180 / Math.PI);
            return new ZoomInfo(MIN_SCALE + RANDOM.nextFloat() * (MAX_SCALE - MIN_SCALE), offsetX, offsetY, angle, RANDOM.nextBoolean(), DRIFT_AMOUNT, DRIFT_TIME, driftAngle);
        }
