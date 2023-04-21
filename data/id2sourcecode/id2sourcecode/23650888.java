    public static void main(String[] argv) throws IOException, SAXException, ParserConfigurationException {
        FileInputStream fis = new FileInputStream("test.xml");
        InputSource is = new InputSource(fis);
        XConfig config = new XConfig(is);
        System.out.println(fis.getChannel().isOpen());
        fis.close();
    }
