    public void setPlotType(String mytmp) {
        plotType = mytmp;
        if (plotType == null) {
            title = "";
            xtitle = "";
            ytitle = "";
            allowDemean = true;
        } else {
            String tmp = new String();
            String filename = new String(plotDir + plotType + ".dft.gz");
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
