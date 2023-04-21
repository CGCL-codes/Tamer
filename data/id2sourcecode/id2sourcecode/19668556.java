            public void actionPerformed(ActionEvent e) {
                int nChan = wireSignalData.getChannelsNumber();
                int nPosSlit = wireSignalData.getPositionsNumberSlit();
                int nPosHarp = wireSignalData.getPositionsNumberHarp();
                int posHarp = ((Integer) useHarpPos_Spinner.getValue()).intValue();
                if (nPosSlit * nChan <= 1) {
                    return;
                }
                int xMin = (int) (GP_sp.getCurrentMinX() + 0.5);
                int xMax = (int) (GP_sp.getCurrentMaxX() + 0.5);
                int yMin = (int) (GP_sp.getCurrentMinY() + 0.5);
                int yMax = (int) (GP_sp.getCurrentMaxY() + 0.5);
                if (xMin >= nPosSlit) {
                    xMin = nPosSlit;
                }
                if (xMin < 0) {
                    xMin = 0;
                }
                if (xMax >= nPosSlit) {
                    xMax = nPosSlit;
                }
                if (xMax < 0) {
                    xMax = 0;
                }
                if (yMin >= nChan) {
                    yMin = nChan;
                }
                if (yMin < 0) {
                    yMin = 0;
                }
                if (yMax >= nChan) {
                    yMax = nChan;
                }
                if (yMax < 0) {
                    yMax = 0;
                }
                for (int ip = xMin; ip < xMax; ip++) {
                    for (int ic = yMin; ic < yMax; ic++) {
                        wireSignalData.setValue(ip, posHarp - 1, ic, 0.);
                    }
                }
                wireSignalData.getPlotData(posHarp - 1).calcMaxMinZ();
                GP_sp.clearZoomStack();
                GP_sp.setColorSurfaceData(wireSignalData.getPlotData(posHarp - 1));
            }
