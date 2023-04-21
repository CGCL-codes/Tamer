        private void mouseDragged(MouseDraggedEvent ev) {
            if (clickPoint == null) return;
            Point pos = ev.getPoint();
            handlePos[selected].x = pos.x;
            double newx = ((double) pos.x - graphBounds.x) / (graphBounds.width - 1.0);
            double newy = ((double) (graphBounds.height - pos.y + graphBounds.y)) / (graphBounds.height - 1.0);
            newy = newy * (maxy - miny) + miny;
            if (newx < 0.0) newx = 0.0;
            if (newx > 1.0) newx = 1.0;
            if (newy < miny) newy = miny;
            if (newy > maxy) newy = maxy;
            y[selected] = 0.001 * ((int) (1000.0 * newy));
            if (selected == 0 || selected == x.length - 1) {
                editModule.calcCoefficients();
                adjustComponents();
                canvas.repaint();
                return;
            }
            x[selected] = 0.001 * ((int) (1000.0 * newx));
            while (x[selected] < x[selected - 1]) {
                double temp = x[selected];
                x[selected] = x[selected - 1];
                x[selected - 1] = temp;
                temp = y[selected];
                y[selected] = y[selected - 1];
                y[selected - 1] = temp;
                Point tempPos = handlePos[selected];
                handlePos[selected] = handlePos[selected - 1];
                handlePos[selected - 1] = tempPos;
                selected--;
            }
            while (x[selected] > x[selected + 1]) {
                double temp = x[selected];
                x[selected] = x[selected + 1];
                x[selected + 1] = temp;
                temp = y[selected];
                y[selected] = y[selected + 1];
                y[selected + 1] = temp;
                Point tempPos = handlePos[selected];
                handlePos[selected] = handlePos[selected + 1];
                handlePos[selected + 1] = tempPos;
                selected++;
            }
            editModule.calcCoefficients();
            adjustComponents();
            canvas.repaint();
        }
