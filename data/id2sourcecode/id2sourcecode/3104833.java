    public String[] getLabels(URL input, String operationName) {
        try {
            URL url = new URL(input.toString() + "?wsdl");
            HttpURLConnection hConn = (HttpURLConnection) url.openConnection();
            hConn.setRequestMethod("GET");
            hConn.setDoOutput(true);
            hConn.setReadTimeout(10000);
            hConn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(hConn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            myXML wsdlDoc = new myXML(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(sb.toString().getBytes()))));
            myXML parsingType = wsdlDoc.findElement("wsdl:definitions");
            String pAdd = "wsdl:";
            if (parsingType == null) pAdd = "";
            myXML portTypes = wsdlDoc.findElement(pAdd + "portType");
            String toLocation = null;
            for (int ops = 0; ops < portTypes.size(); ops++) {
                myXML inner = portTypes.getElement(ops);
                String method = inner.Attribute.find("name");
                if (method.equalsIgnoreCase(operationName)) {
                    myXML inputMess = inner.findElement(pAdd + "input");
                    toLocation = inputMess.Attribute.find("message");
                    break;
                }
            }
            if (toLocation.contains(":")) toLocation = toLocation.substring(toLocation.indexOf(":") + 1);
            String typeLocation = null;
            myXML inner = null;
            for (int ops = 0; ops < wsdlDoc.size(); ops++) {
                inner = wsdlDoc.getElement(ops);
                if (inner.getTag().equalsIgnoreCase(pAdd + "message")) {
                    String method = inner.Attribute.find("name");
                    if (method.equalsIgnoreCase(toLocation)) {
                        typeLocation = inner.getElement(0).Attribute.find("element");
                        break;
                    }
                }
            }
            String[] Labels = null;
            if (typeLocation == null) {
                Labels = new String[inner.size()];
                for (int i = 0; i < inner.size(); i++) {
                    myXML inner2 = inner.getElement(i);
                    Labels[i] = "ns:" + inner2.Attribute.find("name");
                    System.out.println(Labels[i]);
                }
            } else {
                if (typeLocation.contains(":")) typeLocation = typeLocation.substring(typeLocation.indexOf(":") + 1);
                myXML typeElements = wsdlDoc.findElement(pAdd + "types").getElement(0);
                myXML labelList = null;
                for (int ops = 0; ops < typeElements.size(); ops++) {
                    myXML inner2 = typeElements.getElement(ops);
                    String method = inner2.Attribute.find("name");
                    if (method.equalsIgnoreCase(typeLocation)) {
                        labelList = inner2.getElement(0).getElement(0);
                        break;
                    }
                }
                Labels = new String[labelList.size()];
                for (int i = 0; i < labelList.size(); i++) {
                    myXML inner2 = labelList.getElement(i);
                    Labels[i] = "ns:" + inner2.Attribute.find("name");
                    System.out.println(Labels[i]);
                }
            }
            return Labels;
        } catch (Exception ex) {
            return null;
        }
    }
