        @Override
        public void draw(Graphics2D g2) {
            Shape save0 = g2.getClip();
            g2.clipRect(0, 0, width, height);
            if (currentMessage == null) {
                String s = "Open Imperium Galactica";
                int w = commons.text().getTextWidth(10, s);
                commons.text().paintTo(g2, (width - w) / 2, 1, 10, TextRenderer.YELLOW, s);
            } else {
                int mw = getTextWidth();
                int renderX = 0;
                int totalWidth = (mw + width) / 2;
                boolean blink = false;
                if (animationStep <= accelerationStep) {
                    double time = animationStep * 1.0 / frequency;
                    double v0 = 2 * totalWidth * frequency / accelerationStep;
                    double acc = -2.0 * totalWidth * frequency * frequency / accelerationStep / accelerationStep;
                    renderX = (int) (width / 2 + totalWidth - (v0 * time + acc / 2 * time * time));
                } else if (animationStep <= accelerationStep + stayStep) {
                    int time = animationStep - accelerationStep;
                    blink = time % ((int) frequency) < frequency / 2;
                    renderX = width / 2;
                } else if (animationStep <= accelerationStep * 2 + stayStep) {
                    double time = 1.0 * (animationStep - accelerationStep - stayStep) / frequency;
                    double acc = 2.0 * totalWidth * frequency * frequency / accelerationStep / accelerationStep;
                    renderX = (int) (width / 2 - acc / 2 * time * time);
                }
                String msgText = get(currentMessage.text);
                int idx = msgText.indexOf("%s");
                if (idx < 0) {
                    int w = commons.text().getTextWidth(10, msgText);
                    commons.text().paintTo(g2, renderX - w / 2, 1, 10, blink ? TextRenderer.YELLOW : TextRenderer.GREEN, msgText);
                } else {
                    String pre = msgText.substring(0, idx);
                    String post = msgText.substring(idx + 2);
                    String param = null;
                    if (currentMessage.targetPlanet != null) {
                        param = currentMessage.targetPlanet.name;
                    } else if (currentMessage.targetFleet != null) {
                        param = currentMessage.targetFleet.name;
                    } else if (currentMessage.targetProduct != null) {
                        param = currentMessage.targetProduct.name;
                    } else if (currentMessage.targetResearch != null) {
                        param = currentMessage.targetResearch.name;
                    } else if (currentMessage.value != null) {
                        param = currentMessage.value;
                    } else if (currentMessage.label != null) {
                        param = get(currentMessage.label);
                    }
                    int w0 = commons.text().getTextWidth(10, pre);
                    int w1 = commons.text().getTextWidth(10, param);
                    int w2 = commons.text().getTextWidth(10, post);
                    int dx = (w0 + w1 + w2) / 2;
                    int x = renderX - dx;
                    commons.text().paintTo(g2, x, 1, 10, blink ? TextRenderer.YELLOW : TextRenderer.GREEN, pre);
                    commons.text().paintTo(g2, x + w0, 1, 10, blink ? TextRenderer.RED : TextRenderer.YELLOW, param);
                    commons.text().paintTo(g2, x + w0 + w1, 1, 10, blink ? TextRenderer.YELLOW : TextRenderer.GREEN, post);
                }
            }
            g2.setClip(save0);
        }
