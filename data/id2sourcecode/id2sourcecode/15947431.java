    private void handleOutput(OutputStream out) throws Exception {
        boolean active = false;
        int baseX = 0, baseY = 0, scanX = 0, scanY = 0;
        while (!disposed) {
            if (!active) {
                BufferedImage bufImg;
                synchronized (this) {
                    bufImg = robot.createScreenCapture(screenBounds);
                }
                for (int x = 1; x + 602 < bufImg.getWidth(); x++) {
                    for (int y = 1; y + 402 < bufImg.getHeight(); y++) {
                        if (checkCorner(bufImg, x, y, -1, -1, COLOR_RED)) {
                            if (checkCorner(bufImg, x + 601, y, 1, -1, COLOR_GREEN) && checkCorner(bufImg, x, y + 401, -1, 1, COLOR_BLUE) && checkCorner(bufImg, x + 601, y + 401, 1, 1, COLOR_WHITE)) {
                                baseX = x + 1;
                                baseY = y + 1;
                                scanX = scanY = 0;
                                synchronized (this) {
                                    active = true;
                                    robot.keyPress(KeyEvent.VK_X);
                                    robot.keyRelease(KeyEvent.VK_X);
                                    this.active = true;
                                    notifyAll();
                                }
                                break;
                            }
                        }
                    }
                }
                if (!active) {
                    Thread.sleep(1000);
                }
            } else {
                BufferedImage bufImg;
                synchronized (this) {
                    bufImg = robot.createScreenCapture(new Rectangle(baseX, baseY, 600, 400));
                }
                int parsed = 0;
                int col;
                while ((col = bufImg.getRGB(scanX + 3, scanY)) != COLOR_BLACK) {
                    if (col == COLOR_YELLOW) {
                        synchronized (this) {
                            if (bufImg.getRGB(scanX, scanY) == COLOR_YELLOW && bufImg.getRGB(scanX + 1, scanY) == COLOR_RED && bufImg.getRGB(scanX + 2, scanY) == COLOR_RED) {
                                if (sendAcked < HALF_KEYBOARD_BUFFER) throw new IllegalStateException("" + sendAcked);
                                sendAcked -= HALF_KEYBOARD_BUFFER;
                                notifyAll();
                            } else if (bufImg.getRGB(scanX, scanY) == COLOR_YELLOW && bufImg.getRGB(scanX + 1, scanY) == COLOR_YELLOW && bufImg.getRGB(scanX + 2, scanY) == COLOR_YELLOW) {
                                robot.keyPress(KeyEvent.VK_W);
                                robot.keyRelease(KeyEvent.VK_W);
                                robot.keyPress(KeyEvent.VK_X);
                                robot.keyRelease(KeyEvent.VK_X);
                                active = false;
                                this.active = false;
                                break;
                            } else {
                                throw new IllegalStateException();
                            }
                        }
                    } else {
                        int b = 0;
                        for (int i = 0; i < 4; i++) {
                            if (i != 0) col = bufImg.getRGB(scanX + 3 - i, scanY);
                            int newBits = decodeColor(col);
                            if (newBits == -1) throw new IllegalStateException("" + col);
                            b = (b << 2) | newBits;
                        }
                        out.write(b);
                    }
                    parsed++;
                    scanX += 4;
                    if (scanX == 600) {
                        scanY = (scanY + 1) % 400;
                        scanX = 0;
                    }
                }
                if (active && parsed == 0) {
                    out.flush();
                    Thread.sleep(100);
                } else if (active && parsed > 0) {
                    synchronized (this) {
                        robot.keyPress(KeyEvent.VK_W);
                        robot.keyRelease(KeyEvent.VK_W);
                        sendAck(parsed, false);
                    }
                }
            }
        }
    }
