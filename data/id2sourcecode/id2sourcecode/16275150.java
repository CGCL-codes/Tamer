    public void onDraw(Canvas canvas) {
        Paint myPaint = new Paint();
        myPaint.setColor(0xFFDDDDDD);
        Bitmap bmp = ((AndroidDebugDraw) g).mBitmap;
        bmp.eraseColor(0);
        Vec2.creationCount = 0;
        if (currentTest == null) {
            currentTestIndex = 0;
            currentTest = tests.get(currentTestIndex);
            nanoStart = System.nanoTime();
            frameCount = 0;
        }
        if (currentTest.needsReset) {
            TestSettings s = currentTest.settings;
            currentTest.initialize();
            if (s != null) currentTest.settings = s;
            nanoStart = System.nanoTime();
            frameCount = 0;
        }
        currentTest.m_textLine = AbstractExample.textLineHeight;
        g.drawString(5, currentTest.m_textLine, currentTest.getName(), AbstractExample.white);
        currentTest.m_textLine += 2 * AbstractExample.textLineHeight;
        currentTest.step();
        if (currentTest.settings.drawStats) {
            g.drawString(5, currentTest.m_textLine, "Vec2 creations/frame: " + Vec2.creationCount, AbstractExample.white);
            currentTest.m_textLine += AbstractExample.textLineHeight;
        }
        for (int i = 0; i < fpsAverageCount - 1; ++i) {
            nanos[i] = nanos[i + 1];
        }
        nanos[fpsAverageCount - 1] = System.nanoTime();
        float averagedFPS = (float) ((fpsAverageCount - 1) * 1000000000.0 / (nanos[fpsAverageCount - 1] - nanos[0]));
        ++frameCount;
        float totalFPS = (float) (frameCount * 1000000000 / (1.0 * (System.nanoTime() - nanoStart)));
        if (currentTest.settings.drawStats) {
            g.drawString(5, currentTest.m_textLine, "Average FPS (" + fpsAverageCount + " frames): " + averagedFPS, AbstractExample.white);
            currentTest.m_textLine += AbstractExample.textLineHeight;
            g.drawString(5, currentTest.m_textLine, "Average FPS (entire test): " + totalFPS, AbstractExample.white);
            currentTest.m_textLine += AbstractExample.textLineHeight;
        }
        canvas.drawBitmap(bmp, 0, 0, myPaint);
        this.invalidate();
    }
