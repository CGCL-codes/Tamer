        @Override
        public void paint(Graphics g) {
            g2 = (Graphics2D) g;
            g2.drawImage(backgroundBufferImage, 0, 0, null);
            debugDraw.setGraphics(g2);
            mWorld.step(1.0f / 60f, 10);
            if (state == LauncherState.BEFORE_LAUNCH || state == LauncherState.READY) {
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(Color.BLACK);
                g2.drawLine(ROPEPOINT1.x, ROPEPOINT1.y, pig1.getSprite().getCenterX(), pig1.getSprite().getCenterY());
            }
            pig1.draw(g2);
            for (final Bird bird : birds) {
                bird.draw(g2);
            }
            RectWood1.draw(g2);
            CircleWood2.draw(g2);
            BattenWood3.draw(g2);
            BattenWood4.draw(g2);
            BattenWood5.draw(g2);
            RectWood6.draw(g2);
            BattenWood7.draw(g2);
            CircleWood8.draw(g2);
            if (state == LauncherState.BEFORE_LAUNCH || state == LauncherState.READY) {
                g2.drawLine(ROPEPOINT2.x, ROPEPOINT2.y, pig1.getSprite().getCenterX(), pig1.getSprite().getCenterY());
            }
            for (int i = 0; i < fpsAverageCount - 1; ++i) {
                nanos[i] = nanos[i + 1];
            }
            nanos[fpsAverageCount - 1] = System.nanoTime();
            float averagedFPS = (float) ((fpsAverageCount - 1) * 1000000000.0 / (nanos[fpsAverageCount - 1] - nanos[0]));
            ++frameCount;
            float totalFPS = (float) (frameCount * 1000000000 / (1.0 * (System.nanoTime() - nanoStart)));
            g2.drawString("100 Average FPS is " + averagedFPS, 15, 15);
            g2.drawString("Entire FPS is " + totalFPS, 15, 25);
            g2.drawImage(foregroundBufferImage, 0, 0, null);
            g2.dispose();
        }
