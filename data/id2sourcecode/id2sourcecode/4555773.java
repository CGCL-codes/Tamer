    @Override
    public void mouseReleased(MouseEvent e) {
        if (dataX == null) {
            return;
        }
        mouseX2 = e.getX();
        mouseY2 = e.getY();
        int v, v1;
        int lenX = xBoundariesInt.length;
        int lenY = yBoundariesInt.length;
        int[] pixelValues = xBoundariesInt.clone();
        double[] values = xBoundaries.clone();
        for (int i = 0; i < lenX; i++) {
            if (lenX > xBoundariesInt.length) {
                return;
            }
            v = xBoundariesInt[i];
            if (Math.abs(mouseX1 - v) <= 5) {
                if ((mouseX2 - mouseX1) > 0 && (i + 1) < lenX) {
                    v1 = xBoundariesInt[i + 1];
                    if (Math.abs(mouseX2 - v1) <= 5) {
                        xBoundariesInt = new int[lenX - 1];
                        xBoundaries = new double[lenX - 1];
                        for (int r = 0; r < i; r++) {
                            xBoundariesInt[r] = pixelValues[r];
                            xBoundaries[r] = values[r];
                        }
                        for (int r = i; r < lenX - 1; r++) {
                            xBoundariesInt[r] = pixelValues[r + 1];
                            xBoundaries[r] = values[r + 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    } else if (i != 0 && mouseX2 > xBoundariesInt[i - 1] && mouseX2 < xBoundariesInt[i + 1]) {
                        xBoundariesInt[i] = mouseX2;
                        xBoundaries[i] = getValueFromScreenValue(mouseX2, xScale, plotOriginX, xBoundaries[0]);
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
                    }
                } else if ((mouseX2 - mouseX1) < 0 && (i - 1) >= 0) {
                    v1 = xBoundariesInt[i - 1];
                    if (Math.abs(mouseX2 - v1) <= 3) {
                        xBoundariesInt = new int[lenX - 1];
                        xBoundaries = new double[lenX - 1];
                        for (int r = 0; r < i; r++) {
                            xBoundariesInt[r] = pixelValues[r];
                            xBoundaries[r] = values[r];
                        }
                        for (int r = i; r < lenX - 1; r++) {
                            xBoundariesInt[r] = pixelValues[r + 1];
                            xBoundaries[r] = values[r + 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    } else if (i != lenX - 1 && mouseX2 > xBoundariesInt[i - 1] && mouseX2 < xBoundariesInt[i + 1]) {
                        xBoundariesInt[i] = mouseX2;
                        xBoundaries[i] = getValueFromScreenValue(mouseX2, xScale, plotOriginX, xBoundaries[0]);
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
                    }
                }
                if (i == 0) {
                    v1 = xBoundariesInt[1];
                    if ((mouseX2 - mouseX1) > 0 && (mouseX2 < v1)) {
                        xBoundariesInt = new int[lenX + 1];
                        xBoundaries = new double[lenX + 1];
                        xBoundariesInt[0] = pixelValues[0];
                        xBoundaries[0] = values[0];
                        xBoundariesInt[1] = mouseX2;
                        xBoundaries[1] = getValueFromScreenValue(mouseX2, xScale, plotOriginX, xBoundaries[0]);
                        for (int r = 2; r < lenX + 1; r++) {
                            xBoundariesInt[r] = pixelValues[r - 1];
                            xBoundaries[r] = values[r - 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    }
                    break;
                }
                if (i == lenX - 1) {
                    v1 = xBoundariesInt[i - 1];
                    if ((mouseX2 - mouseX1) < 0 && (mouseX2 > v1)) {
                        xBoundariesInt = new int[lenX + 1];
                        xBoundaries = new double[lenX + 1];
                        for (int r = 0; r < lenX - 1; r++) {
                            xBoundariesInt[r] = pixelValues[r];
                            xBoundaries[r] = values[r];
                        }
                        xBoundariesInt[i] = mouseX2;
                        xBoundaries[i] = getValueFromScreenValue(mouseX2, xScale, plotOriginX, xBoundaries[0]);
                        xBoundariesInt[lenX] = pixelValues[lenX - 1];
                        xBoundaries[lenX] = values[lenX - 1];
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    }
                    break;
                }
            }
        }
        pixelValues = yBoundariesInt.clone();
        values = yBoundaries.clone();
        for (int i = 0; i < lenY; i++) {
            if (lenY > yBoundariesInt.length) {
                return;
            }
            v = yBoundariesInt[i];
            if (Math.abs(mouseY1 - v) <= 5) {
                if ((mouseY2 - mouseY1) > 0 && (i + 1) < lenY) {
                    v1 = yBoundariesInt[i + 1];
                    if (Math.abs(mouseY2 - v1) <= 5) {
                        yBoundariesInt = new int[lenY - 1];
                        yBoundaries = new double[lenY - 1];
                        for (int r = 0; r < i; r++) {
                            yBoundariesInt[r] = pixelValues[r];
                            yBoundaries[r] = values[r];
                        }
                        for (int r = i; r < lenY - 1; r++) {
                            yBoundariesInt[r] = pixelValues[r + 1];
                            yBoundaries[r] = values[r + 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    } else if (i != 0 && mouseY2 < yBoundariesInt[i - 1] && mouseY2 > yBoundariesInt[i + 1]) {
                        yBoundariesInt[i] = mouseY2;
                        yBoundaries[i] = getValueFromScreenValue(mouseY2, yScale, plotOriginY, yBoundaries[0]);
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
                    }
                } else if ((mouseY2 - mouseY1) < 0 && (i - 1) >= 0) {
                    v1 = yBoundariesInt[i - 1];
                    if (Math.abs(mouseY2 - v1) <= 3) {
                        yBoundariesInt = new int[lenY - 1];
                        yBoundaries = new double[lenY - 1];
                        for (int r = 0; r < i; r++) {
                            yBoundariesInt[r] = pixelValues[r];
                            yBoundaries[r] = values[r];
                        }
                        for (int r = i; r < lenY - 1; r++) {
                            yBoundariesInt[r] = pixelValues[r + 1];
                            yBoundaries[r] = values[r + 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    } else if (i != lenY - 1 && mouseY2 < yBoundariesInt[i - 1] && mouseY2 > yBoundariesInt[i + 1]) {
                        yBoundariesInt[i] = mouseY2;
                        yBoundaries[i] = getValueFromScreenValue(mouseY2, yScale, plotOriginY, yBoundaries[0]);
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
                    }
                }
                if (i == 0) {
                    v1 = yBoundariesInt[1];
                    if ((mouseY2 - mouseY1) < 0 && (mouseY2 > v1)) {
                        yBoundariesInt = new int[lenY + 1];
                        yBoundaries = new double[lenY + 1];
                        yBoundariesInt[0] = pixelValues[0];
                        yBoundaries[0] = values[0];
                        yBoundariesInt[1] = mouseY2;
                        yBoundaries[1] = getValueFromScreenValue(mouseY2, yScale, plotOriginY, yBoundaries[0]);
                        for (int r = 2; r < lenY + 1; r++) {
                            yBoundariesInt[r] = pixelValues[r - 1];
                            yBoundaries[r] = values[r - 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    }
                    break;
                }
                if (i == lenY - 1) {
                    v1 = yBoundariesInt[i - 1];
                    if ((mouseY2 - mouseY1) > 0 && (mouseY2 < v1)) {
                        yBoundariesInt = new int[lenY + 1];
                        yBoundaries = new double[lenY + 1];
                        for (int r = 0; r < lenY - 1; r++) {
                            yBoundariesInt[r] = pixelValues[r];
                            yBoundaries[r] = values[r];
                        }
                        yBoundariesInt[i] = mouseY2;
                        yBoundaries[i] = getValueFromScreenValue(mouseY2, yScale, plotOriginY, yBoundaries[0]);
                        yBoundariesInt[lenY] = pixelValues[lenY - 1];
                        yBoundaries[lenY] = values[lenY - 1];
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    }
                    break;
                }
            }
        }
        this.repaint();
    }
