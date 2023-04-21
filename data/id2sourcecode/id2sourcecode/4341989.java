    private void Analyse(File f) {
        try {
            JAXBContext jc = JAXBContext.newInstance("org.mcisb.beacon.model");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Result result = (Result) unmarshaller.unmarshal(f);
            ResultTimeSeries timeSeries = result.getResultTimeSeries();
            List<ResultState> resultStates = timeSeries.getResultState();
            times = new double[resultStates.size()];
            for (int i = 0; i < resultStates.size(); i++) {
                ResultState resultState = (ResultState) resultStates.get(i);
                times[i] = resultState.getTimeStamp();
            }
            times = DoubleArrayUtils.divide(times, 60);
            double prestim = 0;
            if (JOptionPane.showConfirmDialog(null, "Is there a pre-stimulation period for this location?", "Pre-stimulation", JOptionPane.YES_NO_OPTION) == 0) {
                for (int i = 1; i < times.length; i++) {
                    if ((times[i] - times[i - 1] > (times[1] + 0.5))) {
                        prestim = times[i];
                        break;
                    }
                }
                for (int i = 0; i < times.length; i++) {
                    times[i] = times[i] - prestim;
                }
            }
            ResultState tempState = (ResultState) resultStates.get(0);
            int channelNumbers = tempState.getCell().get(0).getCellProperty().get(0).getChannel().size();
            int cellNumbers = tempState.getCell().size();
            CellData[] cells = new CellData[cellNumbers];
            String[] cellNames = new String[cellNumbers];
            for (int i = 0; i < cellNumbers; i++) {
                cellNames[i] = tempState.getCell().get(i).getName();
            }
            String[] channelNames = new String[channelNumbers];
            for (int i = 0; i < channelNumbers; i++) {
                channelNames[i] = tempState.getCell().get(0).getCellProperty().get(0).getChannel().get(i).getChannelName();
            }
            for (int i = 0; i < cellNumbers; i++) {
                cells[i] = new CellData(channelNames, cellNames[i]);
            }
            int cyt = 0;
            int nuc = 0;
            int ratio = 99;
            int wholecell = 99;
            tempState = (ResultState) resultStates.get(0);
            List<CellProperty> properties = tempState.getCell().get(0).getCellularCompartment().get(0).getCellProperty();
            for (int i = 0; i < properties.size(); i++) {
                CellProperty tempProperty = (CellProperty) properties.get(i);
                if (tempProperty.getPropertyName().equals("Cyto intensity(Avg)")) {
                    cyt = i;
                    nuc = i;
                }
                if (tempProperty.getPropertyName().equals("Cell intensity(Avg)")) {
                    wholecell = i;
                }
            }
            properties = tempState.getCell().get(0).getCellProperty();
            for (int i = 0; i < properties.size(); i++) {
                CellProperty tempProperty = (CellProperty) properties.get(i);
                if (tempProperty.getPropertyName().equals("Nuc/Cyto ratio(Avg)")) {
                    ratio = i;
                }
            }
            for (int i = 0; i < resultStates.size(); i++) {
                tempState = (ResultState) resultStates.get(i);
                for (int j = 0; j < tempState.getCell().size(); j++) {
                    double[] cytvalue = new double[channelNumbers];
                    double[] nucvalue = new double[channelNumbers];
                    double[] ratiovalue = new double[channelNumbers];
                    double[] cellvalue = new double[channelNumbers];
                    int currentCellIndex = 0;
                    for (int k = 0; k < cellNames.length; k++) {
                        String tempString = tempState.getCell().get(j).getName();
                        if (tempString.equals(cellNames[k])) {
                            currentCellIndex = k;
                        }
                    }
                    for (int k = 0; k < channelNumbers; k++) {
                        cytvalue[k] = tempState.getCell().get(j).getCellularCompartment().get(0).getCellProperty().get(cyt).getChannel().get(k).getChannelIntensity();
                        nucvalue[k] = tempState.getCell().get(j).getCellularCompartment().get(1).getCellProperty().get(nuc).getChannel().get(k).getChannelIntensity();
                        if (ratio != 99) {
                            ratiovalue[k] = tempState.getCell().get(j).getCellProperty().get(ratio).getChannel().get(k).getChannelIntensity();
                        }
                        if (wholecell != 99) {
                            cellvalue[k] = tempState.getCell().get(j).getCellularCompartment().get(0).getCellProperty().get(wholecell).getChannel().get(k).getChannelIntensity();
                        }
                    }
                    cells[currentCellIndex].addCyt(cytvalue);
                    cells[currentCellIndex].addNuc(nucvalue);
                    cells[currentCellIndex].addTime(times[i]);
                    if (wholecell != 99) {
                        cells[currentCellIndex].addCell(cellvalue);
                    }
                    if (ratio != 99) {
                        cells[currentCellIndex].addRatio(ratiovalue);
                    }
                }
            }
            for (int i = 0; i < cells.length; i++) {
                cells[i].finalise();
            }
            myResults = new AnalysisResults();
            try {
                for (int i = 0; i < cells.length; i++) {
                    AnalysedCell tempCell = new AnalysedCell();
                    if (prestim != 0) {
                        tempCell.setPreStimPeriod(new Double(prestim));
                    }
                    tempCell.setCellName(cells[i].getName());
                    for (int j = 0; j < cells[i].numberOfChannels(); j++) {
                        AnalysedChannel tempChannel = new AnalysedChannel();
                        if (cells[i].hasIntensity()) {
                            LinearRegressor lr = new LinearRegressor(cells[i].getTimes(), cells[i].getWholeCellIntensity(j));
                            tempChannel.setDecayRate(lr.getM());
                        }
                        tempChannel.setChannelName(cells[i].getChannelName(j));
                        ArrayList<Double> peakLocations = new ArrayList<Double>();
                        double[] x = cells[i].getTimes();
                        double[] y = cells[i].getRatio(j);
                        double timeShift = 0;
                        if (DoubleArrayUtils.indexOf(y, 0) < x.length && DoubleArrayUtils.indexOf(y, 0) > 0) {
                            y = Arrays.copyOf(y, DoubleArrayUtils.indexOf(y, 0));
                            x = Arrays.copyOf(x, y.length);
                        }
                        if (x[0] == 0) {
                            TimeValuePairs tvp = runIn(x, y);
                            x = tvp.getTimes();
                            y = tvp.getValues();
                            timeShift = tvp.getTimeShift();
                        }
                        if (isDownSlope(y)) {
                            TimeValuePairs tvp = extrapolate(x, y);
                            x = tvp.getTimes();
                            y = tvp.getValues();
                        }
                        double threshold = calcThreshold(x, y);
                        int smoothing = 0;
                        if (smoothCount(y, 8) > smoothCount(y, 10)) {
                            smoothing = 10;
                        } else {
                            smoothing = 8;
                        }
                        double[] widths = new double[80];
                        for (int width = 3; width < 80; width++) {
                            PeakFinder pf = new PeakFinder(x, y, 0.0000000001, threshold, smoothing, width);
                            Peak[] myPeaks = pf.getPeaks();
                            if (pf.hasPeaks()) {
                                widths[width] = myPeaks[0].getPosition();
                            }
                        }
                        int width = findJump(widths);
                        if (width != 999) {
                            goodQuality(width, widths);
                            PeakFinder pf = new PeakFinder(x, y, 0.0000000001, threshold, smoothing, width);
                            Peak[] myPeaks = cleanPeaks(pf.getPeaks());
                            if (myPeaks.length >= 1) {
                                tempChannel.setFirstPeakTime(myPeaks[0].getPosition() - timeShift);
                                peakLocations.add(new Double(myPeaks[0].getPosition() - timeShift));
                                double[] peakHeights = new double[myPeaks.length];
                                double[] peakPositions = new double[myPeaks.length - 1];
                                for (int k = 0; k < myPeaks.length; k++) {
                                    org.mcisb.beacon.model.Peak tempPeak = new org.mcisb.beacon.model.Peak();
                                    tempPeak.setTime(myPeaks[k].getPosition() - timeShift);
                                    tempPeak.setValue(myPeaks[k].getHeight());
                                    peakHeights[k] = myPeaks[k].getHeight();
                                    tempChannel.getPeak().add(tempPeak);
                                }
                                tempChannel.setMaxPeak(DoubleArrayUtils.max(peakHeights));
                                if (myPeaks.length > 1) {
                                    double distances = 0;
                                    for (int k = myPeaks.length - 1; k > 0; k--) {
                                        peakPositions[k - 1] = (myPeaks[k].getPosition() - timeShift) - (myPeaks[k - 1].getPosition() - timeShift);
                                        distances += peakPositions[k - 1];
                                    }
                                    tempChannel.setPeriod(distances / (myPeaks.length - 1));
                                    tempChannel.setPeriodStdDev(Math.sqrt(StatUtils.variance(peakPositions)));
                                }
                            }
                        }
                        tempCell.getAnalysedChannel().add(tempChannel);
                    }
                    myResults.getAnalysedCell().add(tempCell);
                }
            } catch (NotFinalisedException e) {
                e.printStackTrace();
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
