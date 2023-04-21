    public void start() {
        URL base = null;
        try {
            base = this.isActive() ? this.getDocumentBase() : new File(System.getProperty("user.dir")).toURI().toURL();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        try {
            for (int x = 0; x < 360; x += 15) {
                URL url = new URL(base, "hsvapplet/" + dir + "/" + Integer.toString(x) + mode + ".dat");
                double[][] dataArray = ASCIIFile.readDoubleArray(url.openStream());
                java.awt.Color c = HSV.getLineColor(x);
                int size = dataArray.length;
                for (int y = 0; y < size; y++) {
                    plot.addCacheScatterPlot(Integer.toString(x), c, dataArray[y]);
                }
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        plot.setAxisLabels(mode.substring(0, 1), mode.substring(1, 2), mode.substring(2, 3));
        plot.drawCachePlot();
        if (mode.equals("HSV")) {
            plot.setFixedBounds(0, 0, 360);
            plot.setFixedBounds(1, 0, 100);
            plot.setFixedBounds(2, 0, 100);
        } else {
            plot.setFixedBounds(0, 0, 100);
            plot.setFixedBounds(1, 0, 120);
            plot.setFixedBounds(2, 0, 360);
        }
    }
