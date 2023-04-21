    public void setPlotType(String mytmp) {
        plotType = mytmp;
        if (plotType == null) {
            title = "";
            xtitle = "";
            ytitle = "";
            allowDemean = true;
        } else {
            String plotTemp = new String(plotType);
            if (plotType == "north-east-30") {
                plotTemp = "north";
                marksStyle = "dots";
            } else if (plotType == "up-30") {
                plotTemp = "up";
                marksStyle = "dots";
            }
            String tmp = new String();
            String filename = new String(plotDir + plotTemp + ".dft.gz");
            boolean gotInfo = false;
            while (!gotInfo) {
                try {
                    URL dataurl = new URL(docBase, filename);
                    BufferedReader readme = new BufferedReader(new InputStreamReader(new GZIPInputStream(dataurl.openStream())));
                    while (true) {
                        String myline = readme.readLine();
                        if (myline == null) break;
                        if (myline.toLowerCase().startsWith("demean=")) {
                            tmp = myline.substring(7).trim().toLowerCase();
                            if (tmp.compareTo("yes") == 0) {
                                allowDemean = true;
                            } else {
                                allowDemean = false;
                            }
                        } else if (myline.toLowerCase().startsWith("connectpoints=")) {
                            tmp = myline.substring(14).trim().toLowerCase();
                            if (tmp.compareTo("yes") == 0) {
                                connected = true;
                            } else {
                                connected = false;
                            }
                        } else if (myline.toLowerCase().startsWith("title=")) {
                            title = myline.substring(6).trim();
                        } else if (myline.toLowerCase().startsWith("xtitle=")) {
                            xtitle = myline.substring(7).trim();
                        } else if (myline.toLowerCase().startsWith("ytitle=")) {
                            ytitle = myline.substring(7).trim();
                        }
                        if (plotType == "north-east-30") {
                            title = "Horizontal";
                            xtitle = "East (mm)";
                            ytitle = "North (mm)";
                        } else if (plotType == "up-30") {
                            title = "Vertical";
                            xtitle = "Time (decimal year)";
                            ytitle = "Up (mm)";
                        }
                    }
                    gotInfo = true;
                } catch (FileNotFoundException e) {
                    System.err.println("PlotApplet: file not found: " + e);
                } catch (IOException e) {
                    System.err.println("PlotApplet: error reading input file: " + e);
                }
            }
        }
    }
