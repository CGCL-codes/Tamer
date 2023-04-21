    public ResultXmlParser(File f) {
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
            ResultState tempState = (ResultState) resultStates.get(0);
            int channelNumbers = tempState.getCell().get(0).getCellProperty().get(0).getChannel().size();
            int cellNumbers = tempState.getCell().size();
            CellData[] cells = new CellData[cellNumbers];
            String[] channelNames = new String[channelNumbers];
            for (int i = 0; i < channelNumbers; i++) {
                channelNames[i] = tempState.getCell().get(0).getCellProperty().get(0).getChannel().get(i).getChannelName();
            }
            for (int i = 0; i < cellNumbers; i++) {
                cells[i] = new CellData(channelNames, "bob");
            }
            for (int i = 0; i < resultStates.size(); i++) {
                tempState = (ResultState) resultStates.get(i);
                for (int j = 0; j < cellNumbers; j++) {
                    double[] cytvalue = new double[channelNumbers];
                    double[] nucvalue = new double[channelNumbers];
                    for (int k = 0; k < channelNumbers; k++) {
                        cytvalue[k] = tempState.getCell().get(j).getCellularCompartment().get(0).getCellProperty().get(1).getChannel().get(k).getChannelIntensity();
                        nucvalue[k] = tempState.getCell().get(j).getCellularCompartment().get(1).getCellProperty().get(1).getChannel().get(k).getChannelIntensity();
                    }
                    cells[j].addCyt(cytvalue);
                    cells[j].addNuc(nucvalue);
                    if (i == resultStates.size() - 1) {
                        cells[j].finalise();
                    }
                }
            }
            PeakFinder pf;
            try {
                double maxC1 = DoubleArrayUtils.max(cells[0].getRatio(0));
                double maxC2 = DoubleArrayUtils.max(cells[0].getRatio(1));
                double minC1 = DoubleArrayUtils.min(cells[0].getRatio(0));
                double minC2 = DoubleArrayUtils.min(cells[0].getRatio(1));
                double range1 = maxC1 - minC1;
                double range2 = maxC2 - minC2;
                if (range1 > range2) {
                    pf = new PeakFinder(times, cells[0].getRatio(0), 0.0023227, 0.6196, 11, 11);
                } else {
                    pf = new PeakFinder(times, cells[0].getRatio(1), 0.0023227, 0.6196, 11, 11);
                }
                Peak[] myPeaks = pf.getPeaks();
                for (int i = 0; i < myPeaks.length; i++) {
                    System.out.println(i + " " + myPeaks[i].getPosition() + " " + myPeaks[i].getHeight() + " " + myPeaks[i].getWidth());
                }
            } catch (NotFinalisedException nf) {
                System.out.println(nf.getMessage());
            }
            System.out.println();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
