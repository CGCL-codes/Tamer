    protected void actualMap(SimpleGCGCDataset input) throws Exception {
        int numRows = input.getNumberRows();
        int count = 0;
        for (PeakListRow row : input.getAlignment()) {
            if (getStatus() == TaskStatus.CANCELED) {
                break;
            }
            URL url = new URL("http://gmd.mpimp-golm.mpg.de/webservices/wsPrediction.asmx");
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            String xmlFile = this.PredictManyXMLFile((SimplePeakListRowGCGC) row);
            List<String> group = this.getAnswer(xmlFile, httpConn);
            if (group != null) {
                String finalGroup = "";
                for (String name : group) {
                    finalGroup += name + ",";
                }
                ((SimplePeakListRowGCGC) row).setGolmGroup(finalGroup);
                count++;
                progress = (double) count / numRows;
            }
        }
        File file = new File("temporalFile.xml");
        file.delete();
    }
