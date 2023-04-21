    public static HospitalScenario createHospitalScenario(String urlString) throws GeneratorException {
        Document doc = null;
        java.io.InputStream stream = null;
        try {
            URL url = new URL(urlString);
            stream = url.openStream();
        } catch (MalformedURLException e) {
            throw new GeneratorException("MalformedURLException: " + e.getMessage());
        } catch (IOException e) {
            throw new GeneratorException("IOException while opening: " + e.getMessage());
        }
        try {
            InputSource source = new InputSource(stream);
            DOMParser parser = new DOMParser();
            parser.parse(source);
            doc = parser.getDocument();
        } catch (SAXException e) {
            throw new GeneratorException("SAXException: " + e.getMessage());
        } catch (IOException e) {
            throw new GeneratorException("IOException: while instanciating DOM " + e.getMessage());
        }
        HospitalScenarioType hospitalType = new HospitalScenarioType(doc, doc.getDocumentElement());
        return (HospitalScenario) hospitalType.getInstance();
    }
