    public float deintegrate(final float a, final float area) {
        float limit = 1e-6f;
        float bottom = a;
        float top = bottom;
        while (integrate(a, top) < area) {
            top = top == 0 ? 1 : top * 2;
        }
        float testx = (top + bottom) / 2;
        float ta = integrate(a, testx);
        while (Math.abs(ta - area) > limit) {
            assert integrate(a, bottom) < area;
            assert integrate(a, top) > area;
            if (ta < area) {
                bottom = testx;
            } else {
                assert ta > area;
                top = testx;
            }
            testx = (top + bottom) / 2;
            ta = integrate(a, testx);
        }
        return testx;
    }
