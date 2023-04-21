    public void run() {
        Model seriesModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        try {
            URL onturl = new URL(this.ontNameBuffer);
            URLConnection urlc = onturl.openConnection();
            InputStream i = urlc.getInputStream();
            seriesModel.read(i, null);
            this.seriesModelBuffer = seriesModel;
            this.controller.getWindow().getSShell().getDisplay().asyncExec(new Runnable() {

                public void run() {
                    try {
                        readSeriesOntology();
                    } catch (OWL2ConfException e) {
                    }
                }
            });
        } catch (MalformedURLException me) {
            this.notifyObservers(me);
        } catch (IOException ie) {
            this.notifyObservers(ie);
        }
    }
