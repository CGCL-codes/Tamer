    public synchronized void processGLink(SSOSubject authenticated, String fromName, GLinkURL fromURL, String fromRelValue, String toName, GLinkURL toURL, String toRelValue, GLinkAction action) {
        FileOutputStream fileLockStream = null;
        FileLock fileLock = null;
        try {
            File glinkXMLFile = fromURL.getGLinkXMLFile();
            File glinksFileLock = fromURL.getGLinkXMLLockFile();
            boolean causedException = false;
            do {
                if (causedException) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        if (LOG.isLoggable(Level.FINEST)) ex.printStackTrace();
                    }
                }
                try {
                    glinksFileLock.getParentFile().mkdirs();
                    fileLockStream = new FileOutputStream(glinksFileLock);
                    fileLock = fileLockStream.getChannel().lock();
                } catch (FileNotFoundException ex) {
                    causedException = true;
                    if (LOG.isLoggable(Level.FINEST)) {
                        ex.printStackTrace();
                    }
                } catch (IOException ex) {
                    causedException = true;
                    if (LOG.isLoggable(Level.FINEST)) {
                        ex.printStackTrace();
                    }
                } catch (OverlappingFileLockException ex) {
                    causedException = true;
                    if (LOG.isLoggable(Level.FINEST)) {
                        ex.printStackTrace();
                    }
                }
            } while (causedException);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            dbf.setIgnoringElementContentWhitespace(true);
            Document doc = null;
            DocumentBuilder builder = dbf.newDocumentBuilder();
            builder.setErrorHandler(new DefaultHandler2());
            boolean foundToURL = false;
            Element docRoot = null;
            try {
                LOG.finest("Parsing .glink.xml file: " + glinkXMLFile);
                doc = builder.parse(new InputSource(new FileReader(glinkXMLFile)));
                NodeList nodes = doc.getElementsByTagName("g:individual");
                LOG.finest("nodes.getLength(): " + nodes.getLength());
                if (nodes.getLength() > 1) {
                    throw new RuntimeException("SEND MSG TO BROWSER: There are too many <individual/> tags in the glinks xml file located at '" + glinkXMLFile + "'");
                }
                LOG.finest("nodes.item(0): " + nodes.item(0));
                LOG.finest("nodes.item(0).getTextContent(): " + nodes.item(0).getTextContent());
                LOG.finest("nodes.item(0).getTextContent().equals(fromURL.toString()): " + nodes.item(0).getTextContent().equals(fromURL.toString()));
                if (!nodes.item(0).getTextContent().equals(fromURL.toString())) {
                    throw new RuntimeException("SEND MSG TO BROWSER: The individual, '" + nodes.item(0).getTextContent() + "', declared in the glinks xml file, '" + glinkXMLFile + "', does not match the from individual '" + fromURL + "'!!");
                }
                nodes = doc.getElementsByTagName("g:link");
                List<Node> removeNodes = new ArrayList<Node>();
                for (int i = 0; (!foundToURL && i < nodes.getLength()); i++) {
                    LOG.finest("The parent node is: " + nodes.item(i).getNodeName());
                    Node anchor = nodes.item(i).getFirstChild();
                    LOG.finest("The first child node is: " + anchor.getNodeName());
                    if (!anchor.getNodeName().equals("g:glinkAnchor")) {
                        throw new SAXException("Expected a node with name 'g:glinkAnchor' but it was '" + anchor.getNodeName() + "' instead.");
                    }
                    Node glink = anchor.getNextSibling();
                    if (action.equals(GLinkAction.DELETE_ALL_GLINKS)) {
                        removeNodes.add(nodes.item(i));
                    } else if (glink.getTextContent().trim().equals(toURL)) {
                        if (action.equals(GLinkAction.CREATE_GLINK)) {
                            anchor.setTextContent(toName);
                            Node relation = glink.getNextSibling();
                            if ("g:relation".equals(relation.getNodeName())) {
                                relation.setTextContent(fromRelValue + GLinkPattern.RELATION_SEPARATOR + toRelValue);
                            } else {
                                Node newRelation = doc.createElement("g:relation");
                                LOG.finest("2) Creating text node: " + fromRelValue + GLinkPattern.RELATION_SEPARATOR + toRelValue);
                                Node textNode = doc.createTextNode(fromRelValue + GLinkPattern.RELATION_SEPARATOR + toRelValue);
                                newRelation.appendChild(textNode);
                                nodes.item(i).insertBefore(newRelation, relation);
                                relation = newRelation;
                            }
                            Node lastAuthor = relation.getNextSibling();
                            lastAuthor.setTextContent(authenticated.getScreenName());
                            Node lastChange = lastAuthor.getNextSibling();
                            lastChange.setTextContent(isoFormat.format(new Date()));
                        } else if (action.equals(GLinkAction.DELETE_GLINK)) {
                            removeNodes.add(nodes.item(i));
                        }
                        foundToURL = true;
                    }
                }
                for (Node node : removeNodes) {
                    node.getParentNode().removeChild(node);
                }
            } catch (FileNotFoundException ex) {
                if (action.equals(GLinkAction.CREATE_GLINK)) {
                    docRoot = newGLinkDocument(builder, fromURL);
                    doc = docRoot.getOwnerDocument();
                }
                if (LOG.isLoggable(Level.FINEST)) {
                    ex.printStackTrace();
                }
            } catch (SAXException ex) {
                if (LOG.isLoggable(Level.SEVERE)) {
                    ex.printStackTrace();
                }
                docRoot = newGLinkDocument(builder, fromURL);
                doc = docRoot.getOwnerDocument();
                File corruptedGLinksFile = fromURL.getGLinkXMLCorruptedFile();
                glinkXMLFile.renameTo(corruptedGLinksFile);
                LOG.finest("The number of bytes in the corruptedGLinksFile is: " + corruptedGLinksFile.length());
                if (corruptedGLinksFile.length() <= 0) {
                    try {
                        corruptedGLinksFile.delete();
                    } catch (SecurityException exx) {
                        exx.printStackTrace();
                    }
                }
            }
            if (!foundToURL && action.equals(GLinkAction.CREATE_GLINK)) {
                if (docRoot == null) {
                    docRoot = doc.getDocumentElement();
                }
                Element glinkElem = doc.createElement("g:link");
                Element childElem = doc.createElement("g:glinkAnchor");
                LOG.finest("3) Creating text node: " + toName);
                Node textNode = doc.createTextNode(toName);
                childElem.appendChild(textNode);
                glinkElem.appendChild(childElem);
                childElem = doc.createElement("g:glink");
                LOG.finest("4) Creating text node: " + toURL.toString());
                textNode = doc.createTextNode(toURL.toString());
                childElem.appendChild(textNode);
                glinkElem.appendChild(childElem);
                childElem = doc.createElement("g:relation");
                LOG.finest("5) Creating text node: " + fromRelValue + GLinkPattern.RELATION_SEPARATOR + toRelValue);
                textNode = doc.createTextNode(fromRelValue + GLinkPattern.RELATION_SEPARATOR + toRelValue);
                childElem.appendChild(textNode);
                glinkElem.appendChild(childElem);
                childElem = doc.createElement("g:lastAuthorId");
                LOG.finest("6) Creating text node: " + authenticated.getScreenName());
                textNode = doc.createTextNode(authenticated.getScreenName());
                childElem.appendChild(textNode);
                glinkElem.appendChild(childElem);
                childElem = doc.createElement("g:lastChangeDate");
                LOG.finest("7) Creating text node: " + isoFormat.format(new Date()));
                textNode = doc.createTextNode(isoFormat.format(new Date()));
                childElem.appendChild(textNode);
                glinkElem.appendChild(childElem);
                docRoot.appendChild(glinkElem);
            }
            if (doc != null) {
                glinkXMLFile.getParentFile().mkdirs();
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(glinkXMLFile)));
                DOMSource domSource = new DOMSource(doc);
                StreamResult streamResult = new StreamResult(out);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer serializer = tf.newTransformer();
                serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                serializer.setOutputProperty(OutputKeys.INDENT, "no");
                serializer.transform(domSource, streamResult);
                out.close();
                File forceNetworkReload = fromURL.getForceNetworkReloadFile();
                forceNetworkReload.getParentFile().mkdirs();
                forceNetworkReload.createNewFile();
            }
        } catch (ParserConfigurationException ex) {
            if (LOG.isLoggable(Level.FINEST)) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            if (LOG.isLoggable(Level.FINEST)) {
                ex.printStackTrace();
            }
        } catch (TransformerConfigurationException ex) {
            if (LOG.isLoggable(Level.FINEST)) {
                ex.printStackTrace();
            }
        } catch (TransformerException ex) {
            if (LOG.isLoggable(Level.FINEST)) {
                ex.printStackTrace();
            }
        } finally {
            if (fileLockStream != null) {
                try {
                    if (fileLock != null) {
                        fileLock.release();
                    }
                    fileLockStream.close();
                } catch (IOException ex) {
                    if (LOG.isLoggable(Level.FINEST)) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
