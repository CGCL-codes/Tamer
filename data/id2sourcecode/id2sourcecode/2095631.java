            public void actionPerformed(ActionEvent e) {
                GP.setName("Wires' Signals. File: " + readRawDataPanel.getFileName());
                raw_data_file_name = readRawDataPanel.getFileName();
                int nSamples = rawData.getSamplesNumber();
                int samplesStep = nSamples / 50;
                if ((nSamples % 50) > 0) {
                    samplesStep++;
                }
                GP.setLimitsAndTicksX(0., 50., samplesStep, 4);
                plotRawDataPanel.setSpinnerModels(rawData.getChannelsNumber(), rawData.getPositionsNumberSlit(), rawData.getPositionsNumberHarp());
                plotRawDataPanel.setDefaultSpinnersValues();
                plotRawDataPanel.plotRawData();
                if (initializationAnalysisListener != null) {
                    initializationAnalysisListener.actionPerformed(iniAnalysisEvent);
                }
                makeRawToEmittancePanel.initAfterReading();
            }
