    public void buildDom(String sXML) {
        Date currDate = new Date();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("hh_MMM_d_yyyy");
        String dateStr = formatter.format(currDate);
        String fileName = new String("temp" + dateStr + ".xml");
        File newFile = null;
        try {
            newFile = new File(fileName);
            java.io.FileWriter fileOutput = new java.io.FileWriter(newFile);
            fileOutput.write(sXML);
            fileOutput.close();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(fileName);
            } catch (SAXException sxe) {
                Exception x = sxe;
                if (sxe.getException() != null) {
                    x = sxe.getException();
                    x.printStackTrace();
                }
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (newFile.exists()) {
                    newFile.delete();
                }
            }
        } catch (Exception e2) {
            System.out.println("ERROR -- saving to temp file " + fileName + ". " + e2.getMessage() + ".");
        }
    }
