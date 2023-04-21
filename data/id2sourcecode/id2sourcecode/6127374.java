    protected void readandwriteFile(String txtfilename, int simu_endtime) {
        readFiletogreentime();
        for (String linkId : this.links.keySet()) {
            writexmlbody(linkId, simu_endtime);
        }
    }
