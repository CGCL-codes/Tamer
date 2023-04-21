        private void doDelete() {
            if (selected == 0 || selected == x.length - 1) return;
            double newx[] = new double[x.length - 1], newy[] = new double[y.length - 1];
            int i;
            for (i = 0; i < x.length - 1; i++) {
                if (i < selected) {
                    newx[i] = x[i];
                    newy[i] = y[i];
                } else {
                    newx[i] = x[i + 1];
                    newy[i] = y[i + 1];
                }
            }
            selected = 0;
            editModule.x = x = newx;
            editModule.y = y = newy;
            handlePos = new Point[x.length];
            for (i = 0; i < handlePos.length; i++) handlePos[i] = new Point(0, 0);
            editModule.calcCoefficients();
            adjustComponents();
            findRange();
            positionHandles(graphBounds);
            canvas.repaint();
        }
