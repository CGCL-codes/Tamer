        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            int x1, y1, x2, y2, x3, y3;
            int w1 = 0;
            int w2 = getWidth() - 0;
            int h1 = 0;
            int h2 = getHeight() - 0;
            int midw = (w1 + w2) / 2;
            int midh = (h1 + h2) / 2;
            switch(dir) {
                case UP:
                    x1 = w1;
                    y1 = h2;
                    x2 = w2;
                    y2 = h2;
                    x3 = midw;
                    y3 = h1;
                    break;
                case DOWN:
                    x1 = w1;
                    y1 = h1;
                    x2 = w2;
                    y2 = h1;
                    x3 = midw;
                    y3 = h2;
                    break;
                case RIGHT:
                    x1 = w1;
                    y1 = h1;
                    x2 = w2;
                    y2 = midh;
                    x3 = w1;
                    y3 = h2;
                    break;
                default:
                    x1 = w2;
                    y1 = h1;
                    x2 = w1;
                    y2 = midh;
                    x3 = w2;
                    y3 = h2;
                    break;
            }
            int[] xArray = { x1, x2, x3 };
            int[] yArray = { y1, y2, y3 };
            g.fillPolygon(xArray, yArray, 3);
        }
