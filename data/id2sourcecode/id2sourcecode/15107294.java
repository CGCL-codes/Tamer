    private void addOWLURI() {
        String url = JOptionPane.showInputDialog(this, Translator.getTerm("InputURIMessage"), "http://www.yamaguchi.comp.ae.keio.ac.jp/doddle/sample.owl");
        if (url != null) {
            if (!listModel.contains(url)) {
                try {
                    InputStream inputStream = new URL(url).openStream();
                    Model ontModel = Utils.getOntModel(inputStream, "---", getType(url), DODDLEConstants.BASE_URI);
                    addOWLOntology(ontModel, url);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error", "Can not set the selected ontology", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }
