    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == localStylesBtn) {
            plotStyleFrame = new JFrame("Plot Style Settings");
            plotStylePanel = new JPanel();
            plotStylePanel.setLayout(stylePanelLayout);
            stylePanelLayout.add(plotLabelLbl, plotStylePanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(plotLabelTFd, plotStylePanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(lineCBx, plotStylePanel, 0, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(pointCBx, plotStylePanel, 1, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(plotColorBtn, plotStylePanel, 0, 3, 2, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(secantColorBtn, plotStylePanel, 0, 4, 2, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(tangentColorBtn, plotStylePanel, 0, 5, 2, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            plotStyleFrame.getContentPane().setLayout(new GridLayout(1, 0));
            plotStyleFrame.add(plotStylePanel);
            plotStyleFrame.setSize(300, 300);
            plotStyleFrame.setVisible(true);
        } else if (ae.getSource() == eraseCurvesBtn) {
            graphicsPanel.clearCompletely();
            initializeGraphics();
            drawGraphPaper();
            graphicsPanel.setBase();
            drawSecant();
            graphicsPanel.update();
        } else if (ae.getSource() == drawCurvesBtn) {
            draw();
        } else if (ae.getSource() == zoomInBtn && pDrawn) {
            double newXMin = (windowXMin + pX) / 2;
            double newXMax = (windowXMax + pX) / 2;
            double newYMin = (windowYMin + pY) / 2;
            double newYMax = (windowYMax + pY) / 2;
            newXMin = Math.round(1000.0 * newXMin) / 1000.0;
            newXMax = Math.round(1000.0 * newXMax) / 1000.0;
            newYMin = Math.round(1000.0 * newYMin) / 1000.0;
            newYMax = Math.round(1000.0 * newYMax) / 1000.0;
            windowXMinTFd.setText("" + newXMin);
            windowXMaxTFd.setText("" + newXMax);
            windowYMinTFd.setText("" + newYMin);
            windowYMaxTFd.setText("" + newYMax);
            if (inheritRangesCBx.isSelected()) {
                xMinTFd.setText("" + newXMin);
                xMaxTFd.setText("" + newXMax);
            }
            draw();
        } else if (ae.getSource() == zoomOutBtn && pDrawn) {
            double newXMin = 2 * windowXMin - pX;
            double newXMax = 2 * windowXMax - pX;
            double newYMin = 2 * windowYMin - pY;
            double newYMax = 2 * windowYMax - pY;
            newXMin = Math.round(1000.0 * newXMin) / 1000.0;
            newXMax = Math.round(1000.0 * newXMax) / 1000.0;
            newYMin = Math.round(1000.0 * newYMin) / 1000.0;
            newYMax = Math.round(1000.0 * newYMax) / 1000.0;
            windowXMinTFd.setText("" + newXMin);
            windowXMaxTFd.setText("" + newXMax);
            windowYMinTFd.setText("" + newYMin);
            windowYMaxTFd.setText("" + newYMax);
            if (inheritRangesCBx.isSelected()) {
                xMinTFd.setText("" + newXMin);
                xMaxTFd.setText("" + newXMax);
            }
            draw();
        } else if (ae.getSource() == globalStylesBtn) {
            styleFrame = new JFrame("Global Style Settings");
            stylePanel = new JPanel();
            stylePanel.setLayout(stylePanelLayout);
            stylePanelLayout.add(textPlusBtn, stylePanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(textMinusBtn, stylePanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.CENTER);
            styleFrame.getContentPane().setLayout(new GridLayout(1, 0));
            styleFrame.add(stylePanel);
            styleFrame.setSize(300, 300);
            styleFrame.setVisible(true);
        } else if (ae.getSource() == textPlusBtn) {
            currentTextSize++;
            currentMathSize++;
            updateFonts();
        } else if (ae.getSource() == textMinusBtn) {
            currentTextSize--;
            currentMathSize--;
            updateFonts();
        } else if (ae.getSource() == autoscaleBtn) {
            System.out.println("autoscale button pressed");
            this.coordGenerator.autoScale(MathCoords.Y_AXIS);
            double newXMin = this.mathPainter.getXMin();
            double newXMax = newXMin + this.mathPainter.getXRange();
            double newYMin = this.mathPainter.getYMin();
            double newYMax = newYMin + this.mathPainter.getYRange();
            newXMin = Math.round(1000.0 * newXMin) / 1000.0;
            newXMax = Math.round(1000.0 * newXMax) / 1000.0;
            newYMin = Math.round(1000.0 * newYMin) / 1000.0;
            newYMax = Math.round(1000.0 * newYMax) / 1000.0;
            windowXMinTFd.setText("" + newXMin);
            windowXMaxTFd.setText("" + newXMax);
            windowYMinTFd.setText("" + newYMin);
            windowYMaxTFd.setText("" + newYMax);
            draw();
        } else {
            try {
                draw();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
