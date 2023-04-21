    private void performSearch() {
        try {
            if (this.currentIndex > 100) {
                this.iterator = (new java.util.Vector()).iterator();
            } else {
                url = new URL(this.searchURL + "&num=10&start=" + this.currentIndex + "&q=" + this.criteria);
                InputStream input = url.openStream();
                int c;
                while ((c = input.read()) != -1) {
                    result = result + (char) c;
                }
                String googleResultsFile = VueUtil.getDefaultUserFolder().getAbsolutePath() + File.separatorChar + VueResources.getString("save.google.results");
                FileWriter fileWriter = new FileWriter(googleResultsFile);
                fileWriter.write(result);
                fileWriter.close();
                result = "";
                GSP gsp = loadGSP(googleResultsFile);
                java.util.Iterator i = gsp.getRES().getResultList().iterator();
                java.util.Vector resultVector = new java.util.Vector();
                while (i.hasNext()) {
                    Result r = (Result) i.next();
                    Resource resource = Resource.getFactory().get(r.getUrl());
                    if (r.getTitle() != null) resource.setTitle(r.getTitle().replaceAll("</*[a-zA-Z]>", "")); else resource.setTitle(r.getUrl().toString());
                    resultVector.add(new Asset(r.getTitle(), "", r.getUrl()));
                }
                this.iterator = resultVector.iterator();
                this.currentIndex += (resultVector.size() - 1);
            }
        } catch (Throwable t) {
            Utilities.log("cannot connect google");
        }
    }
