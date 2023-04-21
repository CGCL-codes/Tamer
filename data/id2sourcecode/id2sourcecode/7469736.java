        void zoomOutAction() {
            if (zoomIndex == MAX_ZOOM_INDEX) zoomIn.setEnabled(true);
            if (zoomIndex > -MAX_ZOOM_INDEX) {
                double xMin = getXAxis().getAxisMin();
                double xMax = getXAxis().getAxisMax();
                double yMin = getYAxis().getAxisMin();
                double yMax = getYAxis().getAxisMax();
                double xCenter = (xMin + xMax) / 2;
                double yCenter = (yMin + yMax) / 2;
                double dx = (xMax - xMin) / Math.sqrt(2);
                double dy = (yMax - yMin) / Math.sqrt(2);
                getXAxis().setAxisMin(xCenter - dx);
                getXAxis().setAxisMax(xCenter + dx);
                getYAxis().setAxisMin(yCenter - dy);
                getYAxis().setAxisMax(yCenter + dy);
                update();
                zoomIndex--;
            }
            if (zoomIndex == -MAX_ZOOM_INDEX) {
                zoomOut.setEnabled(false);
                zoomIn.setFocus(true);
            } else zoomOut.setFocus(true);
        }
