    public void convertTXTtoXML(String txtfilename, String xmlfilename, int simu_endtime) {
        initialReader(txtfilename);
        initialWriter(xmlfilename);
        readandwriteFile(txtfilename, simu_endtime);
        endReader();
        endWriter();
    }
