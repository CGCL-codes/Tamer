    public static void main(String argv[]) {
        XMLReader saxReader = new GB_Csv2XmlReaderHtml();
        try {
            File f = new File("C:/gb/gb-commons/test/com/loribel/commons/xml/data/data.txt");
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            FileReader fr = new FileReader(f);
            URL l_url = GB_Csv2XmlTest.class.getResource("data/data.txt");
            InputStream is = l_url.openStream();
            InputSource inputSource = new InputSource(is);
            Source source = new SAXSource(saxReader, inputSource);
            StreamResult result = new StreamResult(System.out);
            transformer.setOutputProperty("indent", "yes");
            transformer.transform(source, result);
        } catch (TransformerConfigurationException tce) {
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());
            Throwable x = tce;
            if (tce.getException() != null) {
                x = tce.getException();
            }
            x.printStackTrace();
        } catch (TransformerException te) {
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());
            Throwable x = te;
            if (te.getException() != null) {
                x = te.getException();
            }
            x.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
