    public void computeXSL() {
        if (directory.isDirectory()) {
            File[] dirs = directory.listFiles(new FileFilter() {

                public boolean accept(File file) {
                    return ((file.isDirectory()));
                }
            });
            numberFiles = dirs.length;
            this.setProgress(0);
            String XSL = directoryPath + File.separator + "radioCoverageXSL.xsl";
            String XML_out = directory.getAbsolutePath() + File.separator + "radioOutput.xml";
            XmlFile.writeIntoFile(XML_out, "<list>" + "\n");
            for (int i = 0; i < dirs.length; i++) {
                String XML_in = dirs[i].getAbsolutePath() + File.separator + "radioCoverage.xml";
                String XML_temp_out = dirs[i].getAbsolutePath() + File.separator + "tempRadioOutput.xml";
                try {
                    SaxonFactory.SaxonJob(XSL, XML_in, XML_temp_out);
                    this.setProgress(currentProgress++);
                    XmlFile.copy(XML_temp_out, XML_out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            XmlFile.writeIntoFile(XML_out, "</list>" + "\n");
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
