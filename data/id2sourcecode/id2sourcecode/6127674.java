        public void draw(Graphics g, boolean coordsChanged) {
            int ct = function.getPointCount();
            if (ct == 0) return;
            g.setColor(Color.magenta);
            int xInt, yInt, aInt, bInt;
            double x, y, a, b;
            switch(function.getStyle()) {
                case TableFunction.SMOOTH:
                    {
                        if (ct > 1) {
                            try {
                                x = function.getX(0);
                                y = function.getVal(x);
                                xInt = coords.xToPixel(x);
                                yInt = coords.yToPixel(y);
                                int limit = coords.xToPixel(function.getX(ct - 1));
                                aInt = xInt;
                                while (aInt < limit) {
                                    aInt += 3;
                                    if (aInt > limit) aInt = limit;
                                    a = coords.pixelToX(aInt);
                                    b = function.getVal(a);
                                    bInt = coords.yToPixel(b);
                                    g.drawLine(xInt, yInt, aInt, bInt);
                                    xInt = aInt;
                                    yInt = bInt;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                case TableFunction.PIECEWISE_LINEAR:
                    {
                        x = function.getX(0);
                        xInt = coords.xToPixel(x);
                        y = function.getY(0);
                        yInt = coords.yToPixel(y);
                        for (int i = 1; i < ct; i++) {
                            a = function.getX(i);
                            aInt = coords.xToPixel(a);
                            b = function.getY(i);
                            bInt = coords.yToPixel(b);
                            g.drawLine(xInt, yInt, aInt, bInt);
                            xInt = aInt;
                            yInt = bInt;
                        }
                        break;
                    }
                case TableFunction.STEP:
                    {
                        x = function.getX(0);
                        xInt = coords.xToPixel(x);
                        for (int i = 0; i < ct; i++) {
                            if (i < ct - 1) {
                                double nextX = function.getX(i + 1);
                                a = (x + nextX) / 2;
                                x = nextX;
                            } else a = x;
                            aInt = coords.xToPixel(a);
                            y = function.getY(i);
                            yInt = coords.yToPixel(y);
                            g.drawLine(xInt, yInt, aInt, yInt);
                            xInt = aInt;
                        }
                        break;
                    }
                case TableFunction.STEP_LEFT:
                    {
                        x = function.getX(0);
                        xInt = coords.xToPixel(x);
                        for (int i = 1; i < ct; i++) {
                            a = function.getX(i);
                            aInt = coords.xToPixel(a);
                            y = function.getY(i - 1);
                            yInt = coords.yToPixel(y);
                            g.drawLine(xInt, yInt, aInt, yInt);
                            xInt = aInt;
                        }
                        break;
                    }
                case TableFunction.STEP_RIGHT:
                    {
                        x = function.getX(0);
                        xInt = coords.xToPixel(x);
                        for (int i = 1; i < ct; i++) {
                            a = function.getX(i);
                            aInt = coords.xToPixel(a);
                            y = function.getY(i);
                            yInt = coords.yToPixel(y);
                            g.drawLine(xInt, yInt, aInt, yInt);
                            xInt = aInt;
                        }
                        break;
                    }
            }
            for (int i = 0; i < ct; i++) {
                x = function.getX(i);
                y = function.getY(i);
                xInt = coords.xToPixel(x);
                yInt = coords.yToPixel(y);
                g.fillOval(xInt - 2, yInt - 2, 5, 5);
            }
        }
