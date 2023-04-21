    public ImagePlus load(String directory, String fileName) {
        if (!directory.endsWith(File.separator)) directory += File.separator;
        if ((fileName == null) || (fileName == "")) return null;
        NrrdFileInfo fi;
        try {
            fi = getHeaderInfo(directory, fileName);
        } catch (IOException e) {
            IJ.write("readHeader: " + e.getMessage());
            return null;
        }
        if (IJ.debugMode) IJ.log("fi:" + fi);
        IJ.showStatus("Loading Nrrd File: " + directory + fileName);
        ImagePlus imp;
        FlexibleFileOpener gzfo;
        if (fi.encoding.equals("gzip") && detachedHeader) {
            gzfo = new FlexibleFileOpener(fi, FlexibleFileOpener.GZIP);
            imp = gzfo.open(false);
        } else if (fi.encoding.equals("gzip")) {
            long preOffset = fi.longOffset > 0 ? fi.longOffset : fi.offset;
            fi.offset = 0;
            fi.longOffset = 0;
            gzfo = new FlexibleFileOpener(fi, FlexibleFileOpener.GZIP, preOffset);
            if (IJ.debugMode) IJ.log("gzfo:" + gzfo);
            imp = gzfo.open(false);
        } else {
            FileOpener fo = new FileOpener(fi);
            imp = fo.open(false);
        }
        if (imp == null) return null;
        Calibration cal = imp.getCalibration();
        Calibration spatialCal = this.getCalibration();
        cal.pixelWidth = spatialCal.pixelWidth;
        cal.pixelHeight = spatialCal.pixelHeight;
        cal.pixelDepth = spatialCal.pixelDepth;
        cal.setUnit(spatialCal.getUnit());
        cal.xOrigin = spatialCal.xOrigin;
        cal.yOrigin = spatialCal.yOrigin;
        cal.zOrigin = spatialCal.zOrigin;
        imp.setCalibration(cal);
        return imp;
    }
