        protected void centerText(String s1, Font f1, FontMetrics m1, Color color1, String s2, Font f2, FontMetrics m2, Color color2, Graphics g, int x, int y, int regionWidth, int regionHeight, double stringRotationPiRadians) {
            int stringWidth1 = 0, stringWidth2 = 0, x0 = 0, x1 = 0, y0 = 0, y1 = 0, fontAscent1 = 0, fontHeight1 = 0, fontHeight2 = 0;
            fontHeight1 = m1.getHeight();
            stringWidth1 = m1.stringWidth(s1);
            fontAscent1 = m1.getAscent();
            if (s2 != null) {
                fontHeight2 = m2.getHeight();
                stringWidth2 = m2.stringWidth(s2);
            }
            if ((stringRotationPiRadians == 0.5) || (stringRotationPiRadians == 1.5)) {
                y0 = y - (regionHeight - stringWidth1) / 2;
                y1 = y - (regionHeight - stringWidth2) / 2;
                if (s2 == null) {
                    x0 = x + (regionWidth - fontHeight1) / 2;
                } else {
                    x0 = x + ((regionWidth - (int) (fontHeight1 + (fontHeight2 * 1.2))) / 2);
                    x1 = x0 + (int) (fontHeight2 * 1.2);
                }
            } else {
                x0 = x + (regionWidth - stringWidth1) / 2;
                x1 = x + (regionWidth - stringWidth2) / 2;
                if (s2 == null) {
                    y0 = y + (regionHeight - fontHeight1) / 2 + fontAscent1;
                } else {
                    y0 = y + ((regionHeight - (int) (fontHeight1 + (fontHeight2 * 1.2))) / 2) + fontAscent1;
                    y1 = y0 + (int) (fontHeight2 * 1.2);
                }
            }
            if (g instanceof Graphics2D) {
                Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
                hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).addRenderingHints(hints);
            }
            Font f3 = f1.deriveFont(AffineTransform.getRotateInstance(stringRotationPiRadians * Math.PI));
            g.setFont(f3);
            g.setColor(color1);
            g.drawString(s1, x0, y0);
            if (s2 != null) {
                Font f4 = f2.deriveFont(AffineTransform.getRotateInstance(stringRotationPiRadians * Math.PI));
                g.setFont(f4);
                g.setColor(color2);
                g.drawString(s2, x1, y1);
            }
        }
