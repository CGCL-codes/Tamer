            @Override
            public void run() {
                while (isRunning) {
                    if (effectIndex == EFFECT_IMAGE_ANIMATION) m_xShift += 10; else if (effectIndex == EFFECT_COLOR_ANIMATION && gradient != null) {
                        arg += Math.PI / 10;
                        double cos = Math.cos(arg);
                        double f1 = (1 + cos) / 2;
                        double f2 = (1 - cos) / 2;
                        arg = arg % (Math.PI * 2);
                        Color c1 = gradient.getColor1();
                        Color c2 = gradient.getColor2();
                        int r = (int) (c1.getRed() * f1 + c2.getRed() * f2);
                        r = Math.min(Math.max(r, 0), 255);
                        int g = (int) (c1.getGreen() * f1 + c2.getGreen() * f2);
                        g = Math.min(Math.max(g, 0), 255);
                        int b = (int) (c1.getBlue() * f1 + c2.getBlue() * f2);
                        b = Math.min(Math.max(b, 0), 255);
                        setForeground(new Color(r, g, b));
                    }
                    repaint();
                    try {
                        sleep(m_delay);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
