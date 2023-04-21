    private static void main(String[] args) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            URL url = new URL("http://co01sgd/sgdcent/cnt/vw_ss_ma_empleados_activos_to_xml.asp");
            InputStream inputStream = url.openStream();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList employees = doc.getElementsByTagName("empleado");
            for (int emp = 0; emp < employees.getLength(); emp++) {
                Node fstNode = employees.item(emp);
                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element employee = (Element) fstNode;
                    for (int field = 0; field < employee.getChildNodes().getLength(); field++) {
                        String fieldName = employee.getChildNodes().item(field).getNodeName().trim();
                        String fieldValue = employee.getChildNodes().item(field).getChildNodes().item(0).getNodeValue().trim();
                        System.out.println(fieldName + " >> " + fieldValue);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
