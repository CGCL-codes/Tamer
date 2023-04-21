    public void mouseDraggedAction(MathPainterPanelEvent mppe) {
        mouseDragged = true;
        if (nPoints == 0) {
        } else if (nPoints == 1) {
            xdata[0] = mppe.getMathSpaceX();
            ydata[0] = mppe.getMathSpaceY();
            mathCoords.setPoints(xdata, ydata);
            graphicsPanel.clear();
            mathPainter.setPaint(dataColorBtn.getColor());
            pointPlotter.plot();
        } else {
            double thisX = mppe.getMathSpaceX();
            double thisY = mppe.getMathSpaceY();
            dragXdata = new double[nPoints];
            dragYdata = new double[nPoints];
            if (thisX > previousX && thisX < nextX) {
                for (int i = 0; i < dragIndex; i++) {
                    dragXdata[i] = xdata[i];
                    dragYdata[i] = ydata[i];
                }
                dragXdata[dragIndex] = thisX;
                dragYdata[dragIndex] = thisY;
                for (int i = dragIndex + 1; i < nPoints; i++) {
                    dragXdata[i] = xdata[i];
                    dragYdata[i] = ydata[i];
                }
            } else if (thisX < previousX) {
                int ii = insertIndex(xdata, thisX);
                for (int i = 0; i < ii; i++) {
                    dragXdata[i] = xdata[i];
                    dragYdata[i] = ydata[i];
                }
                dragXdata[ii] = thisX;
                dragYdata[ii] = thisY;
                for (int i = ii + 1; i < dragIndex + 1; i++) {
                    dragXdata[i] = xdata[i - 1];
                    dragYdata[i] = ydata[i - 1];
                }
                for (int i = dragIndex + 1; i < nPoints; i++) {
                    dragXdata[i] = xdata[i];
                    dragYdata[i] = ydata[i];
                }
            } else {
                int ii = insertIndex(xdata, thisX) - 1;
                for (int i = 0; i < dragIndex; i++) {
                    dragXdata[i] = xdata[i];
                    dragYdata[i] = ydata[i];
                }
                for (int i = dragIndex; i < ii; i++) {
                    dragXdata[i] = xdata[i + 1];
                    dragYdata[i] = ydata[i + 1];
                }
                dragXdata[ii] = thisX;
                dragYdata[ii] = thisY;
                for (int i = ii + 1; i < nPoints; i++) {
                    dragXdata[i] = xdata[i];
                    dragYdata[i] = ydata[i];
                }
            }
            mathCoords.setPoints(dragXdata, dragYdata);
            graphicsPanel.clear();
            mathPainter.setPaint(dataColorBtn.getColor());
            pointPlotter.plot();
            drawInterpolants(dragXdata, dragYdata);
        }
        graphicsPanel.update();
    }
