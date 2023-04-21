    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        WriteBiogemeFile writeBiogemeFile = new WriteBiogemeFile();
        writeBiogemeFile.readPopulation();
        writeBiogemeFile.parsePlans();
        writeBiogemeFile.parseClustering();
        writeBiogemeFile.writeBiogemeFile();
    }
