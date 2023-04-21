        @Override
        public void paint(Graphics g) {
            g2 = (Graphics2D) g;
            g2.drawImage(backgroundBufferImage, 0, 0, null);
            debugDraw.setGraphics(g2);
            Pig launchPig = null;
            if (state != LauncherState.UNAVAILABLE) launchPig = pigs[launch_pig]; else {
                if (launch_pig + 1 < PIG_TOTAL_COUNT) {
                    launchPig = pigs[launch_pig + 1];
                    final int x = ORIBALLPOSTION.x - launchPig.getSprite().getCenterX();
                    final int y = ORIBALLPOSTION.y - launchPig.getSprite().getCenterY();
                    final int z = (int) Math.sqrt(x * x + y * y);
                    launchPig.getSprite().setCenterX(launchPig.getSprite().getCenterX() + 5 * x / z);
                    launchPig.getSprite().setCenterY(launchPig.getSprite().getCenterY() + 5 * y / z);
                    if (launchPig.getSprite().getCenterX() >= ORIBALLPOSTION.x || launchPig.getSprite().getCenterY() <= ORIBALLPOSTION.y) {
                        state = LauncherState.READY;
                        launchPig.getSprite().setCenterX(ORIBALLPOSTION.x);
                        launchPig.getSprite().setCenterY(ORIBALLPOSTION.y);
                        ++launch_pig;
                    }
                }
            }
            if (launchPig != null && (state == LauncherState.BEFORE_LAUNCH || state == LauncherState.READY)) {
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(Color.BLACK);
                g2.drawLine(ROPEPOINT1.x, ROPEPOINT1.y, launchPig.getSprite().getCenterX(), launchPig.getSprite().getCenterY());
            }
            for (Pig pig : pigs) {
                pig.draw(g2);
            }
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
            synchronized (lock) {
                int size = smears.size();
                for (int i = 0; i < size; i++) {
                    for (Pig pig : pigs) {
                        if (pig.isBinding()) Sprite.draw(g2, movingCloudSprite.getCurFrame(), (int) smears.get(i).x, (int) smears.get(i).y);
                    }
                }
            }
            if (launchPig != null && (state == LauncherState.BEFORE_LAUNCH || state == LauncherState.READY)) {
                g2.drawLine(ROPEPOINT2.x, ROPEPOINT2.y, launchPig.getSprite().getCenterX(), launchPig.getSprite().getCenterY());
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
            if (state == LauncherState.UNAVAILABLE || state == LauncherState.READY) {
                if (state == LauncherState.UNAVAILABLE && elementCount[BIRD_COUNT_OFFSET] > 0 && elementCount[PIG_COUNT_OFFSET] == 0) {
                    stop = true;
                    new Thread() {

                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(null, "失败", "title", JOptionPane.OK_OPTION);
                        }

                        ;
                    }.start();
                } else if (elementCount[BIRD_COUNT_OFFSET] == 0) {
                    stop = true;
                    new Thread() {

                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(null, "胜利", "title", JOptionPane.OK_OPTION);
                        }

                        ;
                    }.start();
                }
            }
            g2.dispose();
        }
