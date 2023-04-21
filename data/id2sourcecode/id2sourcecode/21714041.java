    public GetRouteResponse getRoute(GetRouteRequest getRouteRequest) throws XmlException {
        String sRouteCalculation = getRouteRequest.getCalculation();
        String sLanguage = getRouteRequest.getLanguage();
        int srid = 31467;
        if (Navigator.startConfig.getSrid() != null) {
            int s = Navigator.startConfig.getSrid().intValue();
            if (s > 0) {
                srid = s;
            }
        }
        String target_epsgCode = "" + Navigator.epsg_code;
        String request_epsgCode = getRouteRequest.getEpsgCode();
        String sStartPosX = getRouteRequest.getStartPosX();
        String sStartPosY = getRouteRequest.getStartPosY();
        String sStartPosZ = getRouteRequest.getStartPosZ();
        String sEndPosX = getRouteRequest.getEndPosX();
        String sEndPosY = getRouteRequest.getEndPosY();
        String sEndPosZ = getRouteRequest.getEndPosZ();
        String sStartAdress = null;
        if (getRouteRequest.getStartAdressTextField() != null) sStartAdress = getRouteRequest.getStartAdressTextField().getText();
        String sEndAdress = null;
        if (getRouteRequest.getEndAdressTextField() != null) sEndAdress = getRouteRequest.getEndAdressTextField().getText();
        JTextField[] viaTextFields = getRouteRequest.getViaTextFields();
        Route route = new Route();
        GetRouteResponse response = new GetRouteResponse();
        XLSDocument xlsResponse = null;
        String errorMessage = "";
        route.setRouteDistanceUnit(getRouteRequest.getDistanceUnit());
        String languageCode = "en";
        sLanguage = sLanguage.toLowerCase();
        String distanceUnit = "KM";
        distanceUnit = route.getRouteDistanceUnit();
        String routePreference = "Fastest";
        if (sRouteCalculation.equalsIgnoreCase(Navigator.i18n.getString("ROUTING_PANEL_FASTEST"))) {
            routePreference = "Fastest";
        } else if (sRouteCalculation.equalsIgnoreCase(Navigator.i18n.getString("ROUTING_PANEL_SHORTEST"))) {
            routePreference = "Shortest";
        } else if (sRouteCalculation.equalsIgnoreCase(Navigator.i18n.getString("ROUTING_PANEL_BY_FOOT"))) {
            routePreference = "Pedestrian";
        } else if (sRouteCalculation.equalsIgnoreCase(Navigator.i18n.getString("ROUTING_PANEL_BY_BICYCLE"))) {
            routePreference = "Bicycle";
        }
        StringBuffer xml_request = new StringBuffer();
        xml_request.append("<XLS version=\"1.1\" xls:lang=\"" + languageCode + "\" xmlns=\"http://www.opengis.net/xls\" xmlns:xls=\"http://www.opengis.net/xls\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:gml=\"http://www.opengis.net/gml\">\n");
        xml_request.append("  <RequestHeader xsi:type=\"xls:RequestHeaderType\" sessionID=\"1234567890\" srsName=\"EPSG:" + srid + "\"/>\n");
        xml_request.append("  <Request xsi:type=\"xls:RequestType\" methodName=\"RouteRequest\" requestID=\"1234567890\" version=\"1.1\">\n");
        xml_request.append("    <DetermineRouteRequest xsi:type=\"xls:DetermineRouteRequestType\" distanceUnit=\"" + distanceUnit + "\">\n");
        xml_request.append("      <RoutePlan>\n");
        xml_request.append("        <RoutePreference>" + routePreference + "</RoutePreference>\n");
        xml_request.append("        <WayPointList>\n");
        String startX = null;
        String startY = null;
        String startZ = null;
        String startEpsgCode = request_epsgCode;
        if (sStartPosX != null && sStartPosX.length() > 0 && sStartPosY != null && sStartPosY.length() > 0) {
            startX = sStartPosX;
            startY = sStartPosY;
            startZ = sStartPosZ;
        } else if ((sStartAdress != null && sStartAdress.length() > 0)) {
            boolean isCoordinate = false;
            try {
                StringTokenizer tok = new StringTokenizer(sStartAdress, ",");
                int numTokens = tok.countTokens();
                startX = "" + Double.parseDouble(tok.nextToken());
                startY = "" + Double.parseDouble(tok.nextToken());
                if (numTokens > 2) {
                    startZ = "" + Double.parseDouble(tok.nextToken());
                }
                isCoordinate = true;
                startEpsgCode = request_epsgCode;
                if (request_epsgCode.equals("4326")) {
                    String tmp = startX;
                    startX = startY;
                    startY = tmp;
                }
            } catch (Exception e) {
            }
            if (!isCoordinate) {
                if (Navigator.openLSReverseGeocoder != null) {
                    Point2d point = Navigator.openLSReverseGeocoder.assertAddress(getRouteRequest.getStartAdressTextField(), errorMessage);
                    if (point != null) {
                        System.out.println("got start from openLSReverseGeocoder: " + point.x + " " + point.y);
                        route.setRouteStartDescription(point.x + " " + point.y);
                        startX = "" + point.x;
                        startY = "" + point.y;
                        startEpsgCode = target_epsgCode;
                    }
                } else {
                    route.setRouteStartDescription(sStartAdress);
                }
            }
        } else errorMessage += "<p>" + Navigator.i18n.getString("ROUTING_PANEL_MESSAGE1_START") + "</p>";
        xml_request.append("          <StartPoint>\n");
        xml_request.append("            <Position xsi:type=\"xls:PositionType\">\n");
        xml_request.append("              <gml:Point srsName=\"EPSG:" + startEpsgCode + "\">\n");
        xml_request.append("                <gml:pos>" + startX + " " + startY);
        if (startZ != null && startZ.length() > 0) {
            route.setRouteStartDescription(startX + " " + startY + " " + startZ);
            xml_request.append(" " + startZ);
        } else {
            route.setRouteStartDescription(startX + " " + startY);
        }
        xml_request.append("</gml:pos>\n");
        xml_request.append("              </gml:Point>\n");
        xml_request.append("            </Position>\n");
        xml_request.append("          </StartPoint>\n");
        if (viaTextFields != null && viaTextFields.length > 0) {
            for (int i = 0; i < viaTextFields.length; i++) {
                JTextField viaTextField = viaTextFields[i];
                if (viaTextField != null) {
                    String via = viaTextField.getText();
                    if (via != null && !via.equals("")) {
                        boolean isCoordinate = false;
                        String viaX = null;
                        String viaY = null;
                        String viaZ = null;
                        String epsgCode = null;
                        epsgCode = request_epsgCode;
                        try {
                            StringTokenizer tok = new StringTokenizer(via, ",");
                            int numTokens = tok.countTokens();
                            viaX = "" + Double.parseDouble(tok.nextToken());
                            viaY = "" + Double.parseDouble(tok.nextToken());
                            if (numTokens > 2) {
                                viaZ = "" + Double.parseDouble(tok.nextToken());
                            }
                            isCoordinate = true;
                            epsgCode = request_epsgCode;
                            if (request_epsgCode.equals("4326")) {
                                String tmp = viaX;
                                viaX = viaY;
                                viaY = tmp;
                            }
                        } catch (Exception e) {
                        }
                        if (!isCoordinate) {
                            Point2d point = Navigator.openLSReverseGeocoder.assertAddress(viaTextField, errorMessage);
                            if (point != null) {
                                viaX = "" + point.x;
                                viaY = "" + point.y;
                                epsgCode = target_epsgCode;
                            }
                        }
                        xml_request.append("          <ViaPoint>\n");
                        xml_request.append("            <Position xsi:type=\"xls:PositionType\">\n");
                        xml_request.append("              <gml:Point srsName=\"EPSG:" + epsgCode + "\">\n");
                        xml_request.append("                <gml:pos>" + viaX + " " + viaY);
                        if (viaZ != null) {
                            xml_request.append(" " + viaZ);
                        }
                        xml_request.append("</gml:pos>\n");
                        xml_request.append("              </gml:Point>\n");
                        xml_request.append("            </Position>\n");
                        xml_request.append("          </ViaPoint>\n");
                    }
                }
            }
        }
        String endX = null;
        String endY = null;
        String endZ = null;
        String endEpsgCode = request_epsgCode;
        if (sEndPosX != null && sEndPosX.length() > 0 && sEndPosY != null && sEndPosY.length() > 0) {
            endX = sEndPosX;
            endY = sEndPosY;
            endZ = sEndPosZ;
        } else if ((sEndAdress != null && sEndAdress.length() > 0)) {
            boolean isCoordinate = false;
            try {
                StringTokenizer tok = new StringTokenizer(sEndAdress, ",");
                int numTokens = tok.countTokens();
                endX = "" + Double.parseDouble(tok.nextToken());
                endY = "" + Double.parseDouble(tok.nextToken());
                if (numTokens > 2) {
                    endZ = "" + Double.parseDouble(tok.nextToken());
                }
                isCoordinate = true;
                endEpsgCode = request_epsgCode;
                if (request_epsgCode.equals("4326")) {
                    String tmp = endX;
                    endX = endY;
                    endY = tmp;
                }
            } catch (Exception e) {
            }
            if (!isCoordinate) {
                if (Navigator.openLSReverseGeocoder != null) {
                    Point2d point = Navigator.openLSReverseGeocoder.assertAddress(getRouteRequest.getEndAdressTextField(), errorMessage);
                    if (point != null) {
                        System.out.println("got end from openLSReverseGeocoder: " + point.x + " " + point.y);
                        route.setRouteEndDescription(point.x + " " + point.y);
                        endX = "" + point.x;
                        endY = "" + point.y;
                        endEpsgCode = target_epsgCode;
                    }
                } else {
                    route.setRouteEndDescription(sEndAdress);
                }
            }
        } else errorMessage += "<p>" + Navigator.i18n.getString("ROUTING_PANEL_MESSAGE1_END") + "</p>";
        xml_request.append("          <EndPoint>\n");
        xml_request.append("            <Position xsi:type=\"xls:PositionType\">\n");
        xml_request.append("              <gml:Point srsName=\"EPSG:" + endEpsgCode + "\">\n");
        xml_request.append("                <gml:pos>" + endX + " " + endY);
        if (endZ != null && endZ.length() > 0) {
            route.setRouteEndDescription(endX + " " + endY + " " + endZ);
            xml_request.append(" " + endZ);
        } else {
            route.setRouteEndDescription(endX + " " + endY);
        }
        xml_request.append("</gml:pos>\n");
        xml_request.append("              </gml:Point>\n");
        xml_request.append("            </Position>\n");
        xml_request.append("          </EndPoint>\n");
        xml_request.append("        </WayPointList>\n");
        xml_request.append("      </RoutePlan>\n");
        xml_request.append("      <RouteInstructionsRequest format=\"text/plain\" provideGeometry=\"true\"/>\n");
        xml_request.append("      <RouteGeometryRequest/>\n");
        if (getRouteRequest.isRequestRouteInstructions()) {
        }
        if (getRouteRequest.isRequestRouteGeometry()) {
        }
        if (getRouteRequest.isRequestRouteMap()) {
            xml_request.append("      <RouteMapRequest>\n");
            xml_request.append("        <Output height=\"400\" width=\"400\" format=\"png\"/>\n");
            xml_request.append("      </RouteMapRequest>\n");
        }
        if (getRouteRequest.isRequestRouteHandle()) {
        }
        xml_request.append("    </DetermineRouteRequest>\n");
        xml_request.append("  </Request>\n");
        xml_request.append("</XLS>\n");
        System.out.println("xml_request:\n" + xml_request);
        if (!errorMessage.equals("")) {
            JLabel label1 = new JLabel("<html><head><style type=\"text/css\"><!--.Stil2 {font-size: 10px;font-weight: bold;}--></style></head><body><span class=\"Stil2\">" + Navigator.i18n.getString("ROUTING_PANEL_MESSAGE2") + "</span></body></html>");
            JLabel label2 = new JLabel("<html><head><style type=\"text/css\"><!--.Stil2 {font-size: 10px;font-weight: normal;}--></style></head><body><span class=\"Stil2\">" + "<br>" + errorMessage + "<br>" + "<p>" + Navigator.i18n.getString("ROUTING_PANEL_MESSAGE3") + "</p>" + "</span></body></html>");
            Object[] objects = { label1, label2 };
            JOptionPane.showMessageDialog(null, objects, Navigator.i18n.getString("INFO_MESSAGE"), JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            if (Navigator.isVerbose()) {
                System.out.println("contacting " + serviceEndPoint);
            }
            URL u = new URL(serviceEndPoint);
            HttpURLConnection urlc = (HttpURLConnection) u.openConnection();
            urlc.setReadTimeout(Navigator.TIME_OUT);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestMethod("POST");
            urlc.setRequestProperty("Content-Type", "application/xml");
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            PrintWriter xmlOut = null;
            xmlOut = new java.io.PrintWriter(urlc.getOutputStream());
            xmlOut.write(xml_request.toString());
            xmlOut.flush();
            xmlOut.close();
            InputStream is = null;
            if (true) {
                is = urlc.getInputStream();
                xlsResponse = XLSDocument.Factory.parse(is);
                is.close();
            } else {
                Reader in = new InputStreamReader(new FileInputStream("C:/Test/RouteInclIndoor_Response2.xml"), "UTF-8");
                xlsResponse = XLSDocument.Factory.parse(in);
            }
        } catch (MalformedURLException mURLe) {
            mURLe.printStackTrace();
            errorMessage += "<p>Malformed URL</p>";
        } catch (IOException ioe) {
            ioe.printStackTrace();
            errorMessage += "<p>IO Exception</p>";
        } catch (XmlException xmle) {
            xmle.printStackTrace();
            errorMessage += "<p>Error occured during parsing the XML response</p>";
        }
        if (!errorMessage.equals("")) {
            System.out.println("\nerrorMessage: " + errorMessage + "\n\n");
            JLabel label1 = new JLabel("<html><head><style type=\"text/css\"><!--.Stil2 {font-size: 10px;font-weight: bold;}--></style></head><body><span class=\"Stil2\">Route Error</span></body></html>");
            JLabel label2 = new JLabel("<html><head><style type=\"text/css\"><!--.Stil2 {font-size: 10px;font-weight: normal;}--></style></head><body><span class=\"Stil2\">" + "<br>" + errorMessage + "<br>" + "<p>please check Java console. If problem persits, please report to system manager</p>" + "</span></body></html>");
            Object[] objects = { label1, label2 };
            JOptionPane.showMessageDialog(null, objects, "Error Message", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        XLSType xlsTypeResponse = xlsResponse.getXLS();
        Node node0 = xlsTypeResponse.getDomNode();
        NodeList nodes1 = node0.getChildNodes();
        for (int i = 0; i < nodes1.getLength(); i++) {
            Node node1 = nodes1.item(i);
            NodeList nodes2 = node1.getChildNodes();
            for (int j = 0; j < nodes2.getLength(); j++) {
                Node node2 = nodes2.item(j);
                String node2Name = node2.getNodeName();
                if (node2Name.equalsIgnoreCase("xls:DetermineRouteResponse")) {
                    net.opengis.xls.DetermineRouteResponseDocument drrd = net.opengis.xls.DetermineRouteResponseDocument.Factory.parse(node2);
                    DetermineRouteResponseType determineRouteResponse = drrd.getDetermineRouteResponse();
                    response.setDetermineRouteResponse(determineRouteResponse);
                } else if (node2Name.equalsIgnoreCase("xls:ErrorList")) {
                    System.out.println("found xls:ErrorList");
                    ErrorListDocument erd = net.opengis.xls.ErrorListDocument.Factory.parse(node2);
                    ErrorListType errorList = erd.getErrorList();
                    response.setErrorList(errorList);
                }
            }
        }
        return response;
    }
