    private static Document parseDocumentForOperator(String operatorWikiName, OperatorDescription opDesc) throws MalformedURLException, ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setIgnoringComments(true);
        builderFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        documentBuilder.setEntityResolver(new XHTMLEntityResolver());
        Document document = null;
        URL url = new URL(WIKI_PREFIX_FOR_OPERATORS + operatorWikiName);
        if (url != null) {
            try {
                document = documentBuilder.parse(url.openStream());
            } catch (IOException e) {
                logger.warning("Could not open " + url.toExternalForm() + ": " + e.getMessage());
            } catch (SAXException e) {
                logger.warning("Could not parse operator documentation: " + e.getMessage());
            }
            int i = 0;
            if (document != null) {
                Element contentElement = document.getElementById("content");
                if (contentElement != null) {
                    contentElement.getParentNode().removeChild(contentElement);
                }
                NodeList bodies = document.getElementsByTagName("body");
                for (int k = 0; k < bodies.getLength(); k++) {
                    Node body = bodies.item(k);
                    while (body.hasChildNodes()) {
                        body.removeChild(body.getFirstChild());
                    }
                    if (k == 0) {
                        body.appendChild(contentElement);
                    }
                }
                NodeList heads = document.getElementsByTagName("head");
                for (int k = 0; k < heads.getLength(); k++) {
                    Node head = heads.item(k);
                    while (head.hasChildNodes()) {
                        head.removeChild(head.getFirstChild());
                    }
                }
                if (heads != null) {
                    while (i < heads.getLength()) {
                        Node head = heads.item(i);
                        head.getParentNode().removeChild(head);
                    }
                }
                Element jumpToNavElement = document.getElementById("jump-to-nav");
                if (jumpToNavElement != null) {
                    jumpToNavElement.getParentNode().removeChild(jumpToNavElement);
                }
                Element mwNormalCatlinksElement = document.getElementById("mw-normal-catlinks");
                if (mwNormalCatlinksElement != null) {
                    mwNormalCatlinksElement.getParentNode().removeChild(mwNormalCatlinksElement);
                }
                Element tocElement = document.getElementById("toc");
                if (tocElement != null) {
                    tocElement.getParentNode().removeChild(tocElement);
                }
                NodeList nodeListDiv = document.getElementsByTagName("div");
                for (int k = 0; k < nodeListDiv.getLength(); k++) {
                    Element div = (Element) nodeListDiv.item(k);
                    if (div.getAttribute("class").equals("printfooter")) {
                        div.getParentNode().removeChild(div);
                    }
                }
                NodeList spanList = document.getElementsByTagName("span");
                for (int k = 0; k < spanList.getLength(); k++) {
                    Element span = (Element) spanList.item(k);
                    if (span.getAttribute("class").equals("editsection")) {
                        span.getParentNode().removeChild(span);
                    }
                }
                boolean doIt = true;
                NodeList pList = document.getElementsByTagName("p");
                for (int k = 0; k < pList.getLength(); k++) {
                    if (doIt) {
                        Node p = pList.item(k);
                        NodeList pChildList = p.getChildNodes();
                        for (int j = 0; j < pChildList.getLength(); j++) {
                            Node pChild = pChildList.item(j);
                            if (pChild.getNodeType() == Node.TEXT_NODE && pChild.getNodeValue() != null && StringUtils.isNotBlank(pChild.getNodeValue()) && StringUtils.isNotEmpty(pChild.getNodeValue())) {
                                String pChildString = pChild.getNodeValue();
                                Element newPWithoutSpaces = document.createElement("p");
                                newPWithoutSpaces.setTextContent(pChildString);
                                Node synopsis = document.createTextNode("Synopsis");
                                Element span = document.createElement("span");
                                span.setAttribute("class", "mw-headline");
                                span.setAttribute("id", "Synopsis");
                                span.appendChild(synopsis);
                                Element h2 = document.createElement("h2");
                                h2.appendChild(span);
                                Element div = document.createElement("div");
                                div.setAttribute("id", "synopsis");
                                div.appendChild(h2);
                                div.appendChild(newPWithoutSpaces);
                                Node pChildParentParent = pChild.getParentNode().getParentNode();
                                Node pChildParent = pChild.getParentNode();
                                pChildParentParent.replaceChild(div, pChildParent);
                                doIt = false;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                NodeList brList = document.getElementsByTagName("br");
                while (i < brList.getLength()) {
                    Node br = brList.item(i);
                    Node parentBrNode = br.getParentNode();
                    parentBrNode.removeChild(br);
                }
                NodeList scriptList = document.getElementsByTagName("script");
                while (i < scriptList.getLength()) {
                    Node scriptNode = scriptList.item(i);
                    Node parentNode = scriptNode.getParentNode();
                    parentNode.removeChild(scriptNode);
                }
                NodeList pList2 = document.getElementsByTagName("p");
                int ccc = 0;
                while (ccc < pList2.getLength()) {
                    Node p = pList2.item(ccc);
                    NodeList pChilds = p.getChildNodes();
                    int kk = 0;
                    while (kk < pChilds.getLength()) {
                        Node pChild = pChilds.item(kk);
                        if (pChild.getNodeType() == Node.TEXT_NODE) {
                            String pNodeValue = pChild.getNodeValue();
                            if (pNodeValue == null || StringUtils.isBlank(pNodeValue) || StringUtils.isEmpty(pNodeValue)) {
                                kk++;
                            } else {
                                ccc++;
                                break;
                            }
                        } else {
                            ccc++;
                            break;
                        }
                        if (kk == pChilds.getLength()) {
                            Node parentBrNode = p.getParentNode();
                            parentBrNode.removeChild(p);
                        }
                    }
                }
                Element firstHeadingElement = document.getElementById("firstHeading");
                if (firstHeadingElement != null) {
                    CURRENT_OPERATOR_NAME_READ_FROM_RAPIDWIKI = firstHeadingElement.getFirstChild().getNodeValue();
                    firstHeadingElement.getParentNode().removeChild(firstHeadingElement);
                }
                Element siteSubElement = document.getElementById("siteSub");
                if (siteSubElement != null) {
                    siteSubElement.getParentNode().removeChild(siteSubElement);
                }
                Element contentSubElement = document.getElementById("contentSub");
                if (contentSubElement != null) {
                    contentSubElement.getParentNode().removeChild(contentSubElement);
                }
                Element catlinksElement = document.getElementById("catlinks");
                if (catlinksElement != null) {
                    catlinksElement.getParentNode().removeChild(catlinksElement);
                }
                NodeList aList = document.getElementsByTagName("a");
                if (aList != null) {
                    int k = 0;
                    while (k < aList.getLength()) {
                        Node a = aList.item(k);
                        Element aElement = (Element) a;
                        if (aElement.getAttribute("class").equals("internal")) {
                            a.getParentNode().removeChild(a);
                        } else {
                            Node aChild = a.getFirstChild();
                            if (aChild != null && (aChild.getNodeValue() != null && aChild.getNodeType() == Node.TEXT_NODE && StringUtils.isNotBlank(aChild.getNodeValue()) && StringUtils.isNotEmpty(aChild.getNodeValue()) || aChild.getNodeName() != null)) {
                                Element aChildElement = null;
                                if (aChild.getNodeName().startsWith("img")) {
                                    aChildElement = (Element) aChild;
                                    Element imgElement = document.createElement("img");
                                    imgElement.setAttribute("alt", aChildElement.getAttribute("alt"));
                                    imgElement.setAttribute("class", aChildElement.getAttribute("class"));
                                    imgElement.setAttribute("height", aChildElement.getAttribute("height"));
                                    imgElement.setAttribute("src", WIKI_PREFIX_FOR_IMAGES + aChildElement.getAttribute("src"));
                                    imgElement.setAttribute("width", aChildElement.getAttribute("width"));
                                    imgElement.setAttribute("border", "1");
                                    Node aParent = a.getParentNode();
                                    aParent.replaceChild(imgElement, a);
                                } else {
                                    k++;
                                }
                            } else {
                                a.getParentNode().removeChild(a);
                            }
                        }
                    }
                }
            }
        }
        return document;
    }
