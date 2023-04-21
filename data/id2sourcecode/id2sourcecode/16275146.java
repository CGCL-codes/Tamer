    private void init(Context context) {
        g = new AndroidDebugDraw();
        for (int i = 0; i < 100; ++i) {
            this.requestFocus();
        }
        registerExample(new VaryingFriction(this));
        registerExample(new Pyramid(this));
        registerExample(new VaryingRestitution(this));
        nanos = new long[fpsAverageCount];
        long nanosPerFrameGuess = (long) (1000000000.0 / targetFPS);
        nanos[fpsAverageCount - 1] = System.nanoTime();
        for (int i = fpsAverageCount - 2; i >= 0; --i) {
            nanos[i] = nanos[i + 1] - nanosPerFrameGuess;
        }
        nanoStart = System.nanoTime();
    }
