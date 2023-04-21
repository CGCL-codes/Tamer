    private void priResumption() throws OAIException {
        String rt = priGetResumptionToken();
        if (rt.length() == 0) {
            return;
        }
        int prevCount = priGetSetCount();
        iCount = -1;
        iResumptionCount++;
        try {
            URL url = new URL(strBaseURL + "?verb=" + strVerb + "&resumptionToken=" + URLEncoder.encode(rt, "UTF-8"));
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http = oParent.frndTrySend(http);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            if (oParent.getValidation() == OAIRepository.VALIDATION_VERY_STRICT) {
                docFactory.setValidating(true);
            } else {
                docFactory.setValidating(false);
            }
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            try {
                xml = docBuilder.parse(http.getInputStream());
                boolValidResponse = true;
            } catch (IllegalArgumentException iae) {
                throw new OAIException(OAIException.CRITICAL_ERR, iae.getMessage());
            } catch (SAXException se) {
                if (oParent.getValidation() != OAIRepository.VALIDATION_LOOSE) {
                    throw new OAIException(OAIException.XML_PARSE_ERR, se.getMessage() + " Try loose validation.");
                } else {
                    try {
                        http.disconnect();
                        url = new URL(strBaseURL + "?verb=" + strVerb + "&resumptionToken=" + URLEncoder.encode(rt, "UTF-8"));
                        http = (HttpURLConnection) url.openConnection();
                        http = oParent.frndTrySend(http);
                        xml = docBuilder.parse(priCreateDummyResponse(http.getInputStream()));
                    } catch (SAXException se2) {
                        throw new OAIException(OAIException.XML_PARSE_ERR, se2.getMessage());
                    }
                }
            }
            try {
                namespaceNode = xml.createElement(strVerb);
                namespaceNode.setAttribute("xmlns:oai", OAIRepository.XMLNS_OAI + strVerb);
                namespaceNode.setAttribute("xmlns:dc", OAIRepository.XMLNS_DC);
                PrefixResolverDefault prefixResolver = new PrefixResolverDefault(namespaceNode);
                XPath xpath = new XPath("/oai:" + strVerb, null, prefixResolver, XPath.SELECT, null);
                XPathContext xpathSupport = new XPathContext();
                int ctxtNode = xpathSupport.getDTMHandleFromNode(xml);
                XObject list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                Node node = list.nodeset().nextNode();
                if (node == null) {
                    namespaceNode.setAttribute("xmlns:oai", OAIRepository.XMLNS_OAI_2_0);
                    prefixResolver = new PrefixResolverDefault(namespaceNode);
                    xpath = new XPath("/oai:OAI-PMH", null, prefixResolver, XPath.SELECT, null);
                    list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                    node = list.nodeset().nextNode();
                    if (node == null) {
                        namespaceNode.setAttribute("xmlns:oai", OAIRepository.XMLNS_OAI_1_0 + strVerb);
                    } else {
                        xpath = new XPath("oai:OAI-PMH/oai:error", null, prefixResolver, XPath.SELECT, null);
                        list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                        NodeList nl = list.nodelist();
                        if (nl.getLength() > 0) {
                            oParent.frndSetErrors(nl);
                            throw new OAIException(OAIException.OAI_ERR, oParent.getLastOAIError().getCode() + ": " + oParent.getLastOAIError().getReason());
                        }
                    }
                }
                xpath = new XPath("//oai:" + strVerb + "/oai:" + priGetMainNodeName(), null, prefixResolver, XPath.SELECT, null);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                nodeList = list.nodelist();
                oParent.frndSetNamespaceNode(namespaceNode);
                xpath = new XPath("//oai:requestURL | //oai:request", null, prefixResolver, XPath.SELECT, null);
                node = xpath.execute(xpathSupport, ctxtNode, prefixResolver).nodeset().nextNode();
                if (node != null) {
                    oParent.frndSetRequest(node);
                }
                oParent.frndSetResponseDate(getResponseDate());
                iRealCursor += prevCount;
                prefixResolver = null;
                xpathSupport = null;
                xpath = null;
            } catch (TransformerException te) {
                throw new OAIException(OAIException.CRITICAL_ERR, te.getMessage());
            } catch (IllegalArgumentException iae) {
                throw new OAIException(OAIException.CRITICAL_ERR, iae.getMessage());
            }
            docFactory = null;
            docBuilder = null;
            url = null;
        } catch (MalformedURLException mue) {
            throw new OAIException(OAIException.CRITICAL_ERR, mue.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new OAIException(OAIException.CRITICAL_ERR, ex.getMessage());
        } catch (FactoryConfigurationError fce) {
            throw new OAIException(OAIException.CRITICAL_ERR, fce.getMessage());
        } catch (ParserConfigurationException pce) {
            throw new OAIException(OAIException.CRITICAL_ERR, pce.getMessage());
        } catch (IOException ie) {
            throw new OAIException(OAIException.CRITICAL_ERR, ie.getMessage());
        }
        iIndex = 1;
    }
