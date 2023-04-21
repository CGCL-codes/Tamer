    protected void actualMap(SimpleGCGCDataset input) throws Exception {
        int numRows = input.getNumberRows();
        int count = 0;
        for (PeakListRow row : input.getAlignment()) {
            if (getStatus() == TaskStatus.CANCELED) {
                break;
            }
            if (((SimplePeakListRowGCGC) row).getMolClass() != null && ((SimplePeakListRowGCGC) row).getMolClass().length() != 0 && !((SimplePeakListRowGCGC) row).getMolClass().contains("NA") && !((SimplePeakListRowGCGC) row).getMolClass().contains("null")) {
                count++;
                continue;
            }
            URL url = new URL("http://gmd.mpimp-golm.mpg.de/webservices/wsLibrarySearch.asmx");
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            String xmlFile = this.PredictManyXMLFile((SimplePeakListRowGCGC) row);
            List<String> group = this.getAnswer(xmlFile, httpConn);
            if (group != null) {
                String finalGroup = "";
                for (int i = 0; i < group.size(); i++) {
                    finalGroup += group.get(i) + ",";
                    if (i == 2) {
                        break;
                    }
                }
                ((SimplePeakListRowGCGC) row).setMolClass(finalGroup);
                count++;
                progress = (double) count / numRows;
            } else {
                break;
            }
        }
        File file = new File("temporalFile.xml");
        file.delete();
    }
