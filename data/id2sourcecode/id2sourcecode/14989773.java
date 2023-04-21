    public void mouseClickedAction(MathPainterPanelEvent mppe) {
        if (clickAction == ADD_POINT) {
            if (nPoints == 0) {
                xdata = new double[1];
                ydata = new double[1];
                xdata[0] = mppe.getMathSpaceX();
                ydata[0] = mppe.getMathSpaceY();
            } else {
                double newX = mppe.getMathSpaceX();
                double newY = mppe.getMathSpaceY();
                double[] newXData = new double[nPoints + 1];
                double[] newYData = new double[nPoints + 1];
                int ii = insertIndex(xdata, newX);
                for (int i = 0; i < ii; i++) {
                    newXData[i] = xdata[i];
                    newYData[i] = ydata[i];
                }
                newXData[ii] = newX;
                newYData[ii] = newY;
                for (int i = ii + 1; i < nPoints + 1; i++) {
                    newXData[i] = xdata[i - 1];
                    newYData[i] = ydata[i - 1];
                }
                xdata = newXData;
                ydata = newYData;
            }
            nPoints++;
            mathCoords.setPoints(xdata, ydata);
            graphicsPanel.clear();
            mathPainter.setPaint(dataColorBtn.getColor());
            pointPlotter.plot();
            if (nPoints > 1) {
                drawInterpolants(xdata, ydata);
            }
            graphicsPanel.update();
        } else {
            if (nPoints == 0) {
            } else {
                int deleteIndex = -1;
                for (int i = 0; i < nPoints; i++) {
                    if ((Math.abs(mppe.getUserSpaceX() - mathPainter.mathToUserX(xdata[i])) <= 5) && (Math.abs(mppe.getUserSpaceY() - mathPainter.mathToUserY(ydata[i])) <= 5)) {
                        deleteIndex = i;
                        break;
                    }
                }
                double[] newXData = new double[nPoints - 1];
                double[] newYData = new double[nPoints - 1];
                for (int i = 0; i < deleteIndex; i++) {
                    newXData[i] = xdata[i];
                    newYData[i] = ydata[i];
                }
                for (int i = deleteIndex; i < nPoints - 1; i++) {
                    newXData[i] = xdata[i + 1];
                    newYData[i] = ydata[i + 1];
                }
                xdata = newXData;
                ydata = newYData;
                nPoints--;
                mathCoords.setPoints(xdata, ydata);
                graphicsPanel.clear();
                mathPainter.setPaint(dataColorBtn.getColor());
                pointPlotter.plot();
                if (nPoints > 1) {
                    drawInterpolants(xdata, ydata);
                }
                graphicsPanel.update();
            }
        }
    }
