    public void convertFiles() {
        File fInputLog = new File(getInputFileName());
        if (!fInputLog.exists()) {
            MessageBox mbErr = new MessageBox("No input", "File " + getInputFileName() + " does not exist. Select an existing log file", MessageBox.MBOK);
            int resp = mbErr.execute();
            return;
        }
        File fOutputX = new File(getOutputFileName());
        if (fOutputX.exists()) {
            MessageBox mbErr = new MessageBox("Output file exists", "Output file " + getOutputFileName() + " already exists. It will be overwritten.\n" + " Cancel does not overwrite the file.", MessageBox.MBOKCANCEL);
            int resp = mbErr.execute();
            if (resp == MessageBox.IDCANCEL) {
                System.out.println("Cancelled");
                return;
            }
        }
        bGoto.modify(ControlConstants.Disabled, 0);
        bConvert.modify(ControlConstants.Disabled, 0);
        this.bConvert.setText("Reading...");
        TrackRecord tr = null;
        Vector vTr = null;
        this.bConvert.setText("Converting...");
        if (cOneTrack.getState()) {
            tr = TrackRecord.readLogFile(fInputLog, null, TrackRecord.OPTION_SINGLETRACK);
        } else {
            tr = TrackRecord.readLogFile(fInputLog, null, TrackRecord.OPTION_SEPARATETRACK);
            if (tr != null) {
                vTr = tr.makeSplitTracks();
            }
        }
        if (tr != null) {
            this.bConvert.setText("Writing...");
            switch(cbgFormat.getSelectedIndex()) {
                case 0:
                    ExporterToGPX expG;
                    if (vTr == null) {
                        expG = new ExporterToGPX(tr);
                    } else {
                        expG = new ExporterToGPX(vTr);
                    }
                    expG.exportToFile(fOutputX, 0);
                    break;
                case 1:
                    tr.writeLogFile(fOutputX);
                    break;
                case 2:
                    ExporterToKML expK;
                    if (vTr == null) {
                        expK = new ExporterToKML(tr);
                    } else {
                        expK = new ExporterToKML(vTr);
                    }
                    int typeExport = 0;
                    switch(cbgColorData.getSelectedIndex()) {
                        case 0:
                            typeExport = ExporterToKML.DF_ALTITUDE;
                            break;
                        case 1:
                            typeExport = ExporterToKML.DF_TIME;
                            break;
                        case 2:
                            typeExport = ExporterToKML.DF_SPEED;
                            break;
                    }
                    expK.exportToFile(fOutputX, typeExport);
                    break;
            }
        } else {
            (new MessageBox("Error", "Error reading input file\nOperation cancelled.", MessageBox.MBOK)).execute();
        }
        bConvert.modify(0, ControlConstants.Disabled);
        this.bConvert.setText("Convert");
        bGoto.modify(0, ControlConstants.Disabled);
    }
