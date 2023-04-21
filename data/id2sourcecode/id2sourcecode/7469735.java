        void zoomInAction() {
            if (zoomIndex == -MAX_ZOOM_INDEX) zoomOut.setEnabled(true);
            if (zoomIndex < MAX_ZOOM_INDEX) {
                double xMin = getXAxis().getAxisMin();
                double xMax = getXAxis().getAxisMax();
                double yMin = getYAxis().getAxisMin();
                double yMax = getYAxis().getAxisMax();
                double xCenter = (xMin + xMax) / 2;
                double yCenter = (yMin + yMax) / 2;
                double dx = (xMax - xMin) / (2 * Math.sqrt(2));
                double dy = (yMax - yMin) / (2 * Math.sqrt(2));
                getXAxis().setAxisMin(xCenter - dx);
                getXAxis().setAxisMax(xCenter + dx);
                getYAxis().setAxisMin(yCenter - dy);
                getYAxis().setAxisMax(yCenter + dy);
                update();
                zoomIndex++;
            }
            if (zoomIndex == MAX_ZOOM_INDEX) {
                zoomIn.setEnabled(false);
                zoomOut.setFocus(true);
            } else zoomIn.setFocus(true);
        }
