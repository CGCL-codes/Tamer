    @Override
    public void run() {
        try {
            if (UipServer.getUipsInstance().getInstancesManager() != null) {
                if (UipServer.getUipsInstance().getUipsStatus() != UipsStatus.Started) {
                    throw new UipsException(String.format(Messages.getString("ErrorUipsNotInitializedBadStatus"), UipServer.getUipsInstance().getUipsStatus()));
                }
            } else {
                throw new UipsException(Messages.getString("ErrorUipsNotInitializedNoManager"));
            }
            StringBuilder receivedText = new StringBuilder(Consts.XmlClientBufferSize * 5);
            ByteBuffer receiveBuffer = ByteBuffer.allocateDirect(Consts.XmlClientBufferSize);
            Validator validator = Settings.getXmlReaderSettings().newValidator();
            while (started) {
                receiveBuffer.clear();
                int receivedBytesCount = socket.read(receiveBuffer);
                if (receivedBytesCount == -1) {
                    Log.write(Level.WARNING, "XmlClient", "Receive", String.format(Messages.getString("XmlClientClosedSocket"), remoteAddress), this);
                    break;
                }
                receiveBuffer.flip();
                receivedText.append(decoder.decode(receiveBuffer).toString());
                Log.write(Level.INFO, "XmlClient", "Receive", String.format(Messages.getString("XmlClientReceivedBytes"), receivedBytesCount, remoteAddress), this);
                String xmlEnd = "</" + Consts.ElementNameUIProtocol + ">";
                int indexEnd = receivedText.indexOf(xmlEnd + Consts.NullByteChar);
                int indexStart = 0;
                if (indexEnd != -1) {
                    indexEnd += xmlEnd.length();
                    indexStart = indexEnd + 1;
                }
                if (indexEnd == -1) {
                    indexEnd = receivedText.indexOf(String.valueOf(Consts.NullByteChar));
                    if (indexEnd != -1) {
                        indexStart = indexEnd + 1;
                    }
                    if (indexEnd == -1) {
                        indexEnd = receivedText.indexOf(xmlEnd);
                        if (indexEnd != -1) {
                            indexEnd += xmlEnd.length();
                            indexStart = indexEnd;
                        }
                    }
                }
                while (indexEnd != -1) {
                    String textToDecode = receivedText.substring(0, indexEnd);
                    receivedText = new StringBuilder(receivedText.substring(indexStart, receivedText.length()));
                    if (!textToDecode.equals("")) {
                        Log.write(Level.FINE, "XmlClient", "Receive", String.format(Messages.getString("XmlReceived"), textToDecode), this);
                        try {
                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            Document document = dBuilder.parse(new ByteArrayInputStream(textToDecode.getBytes("UTF-8")));
                            document.getDocumentElement().normalize();
                            if (document.getDocumentElement().getNodeType() == Node.ELEMENT_NODE && document.getDocumentElement().getNodeName().equals(Consts.ElementNameUIProtocol)) {
                                if (document.getDocumentElement().hasAttribute("xmlns")) {
                                    document.getDocumentElement().removeAttribute("xmlns");
                                }
                                if (document.getDocumentElement().hasAttribute("xmlns:xsi")) {
                                    document.getDocumentElement().removeAttribute("xmlns:xsi");
                                }
                                if (document.getDocumentElement().hasAttribute("xsi:noNamespaceSchemaLocation")) {
                                    document.getDocumentElement().removeAttribute("xsi:noNamespaceSchemaLocation");
                                }
                            }
                            DOMSource domSource = new DOMSource(document);
                            validator.validate(domSource);
                            parseXmlRequest(document);
                        } catch (ParserConfigurationException ex) {
                            Log.write(Level.SEVERE, "XmlClient", "Receive", ex.toString(), this);
                        } catch (SAXParseException ex) {
                            Log.write(Level.SEVERE, "XmlClient", "Receive", ex.toString(), this);
                            sendErrorResponse("XmlException", Consts.PropertyValueError, String.format(Messages.getString("ErrorParseXml"), ex.toString(), textToDecode));
                        } catch (SAXException ex) {
                            Log.write(Level.SEVERE, "XmlClient", "Receive", String.format("%s,%s", Consts.ErrorXmlSchemaValidation, ex.toString()), this);
                            sendErrorResponse(Consts.ErrorXmlSchemaValidation, Consts.PropertyValueError, ex.toString());
                        } catch (Exception ex) {
                            Log.write(Level.SEVERE, "XmlClient", "Receive", String.format("%s,%s", Consts.ErrorXmlSchemaValidation, ex.toString()), this);
                            sendErrorResponse(Consts.ErrorXmlSchemaValidation, Consts.PropertyValueError, ex.toString());
                        }
                    }
                    indexEnd = receivedText.indexOf(xmlEnd + Consts.NullByteChar);
                    indexStart = 0;
                    if (indexEnd != -1) {
                        indexEnd += xmlEnd.length();
                        indexStart = indexEnd + 1;
                    }
                    if (indexEnd == -1) {
                        indexEnd = receivedText.indexOf(String.valueOf(Consts.NullByteChar));
                        if (indexEnd != -1) {
                            indexStart = indexEnd + 1;
                        }
                        if (indexEnd == -1) {
                            indexEnd = receivedText.indexOf(xmlEnd);
                            if (indexEnd != -1) {
                                indexEnd += xmlEnd.length();
                                indexStart = indexEnd;
                            }
                        }
                    }
                }
            }
        } catch (UipsException ex) {
            sendErrorResponse(Consts.ErrorServerNotReady, Consts.PropertyValueError, ex.toString());
            Log.write(Level.SEVERE, "XmlClient", "Receive", ex.toString(), this);
        } catch (AsynchronousCloseException ex) {
            Log.write(Level.WARNING, "XmlClient", "Receive", Messages.getString("XmlClientViolentlyDisconected"), this);
        } catch (IOException ex) {
            Log.write(Level.SEVERE, "XmlClient", "Receive", String.format(Messages.getString("XmlClientSocketException"), ex.toString()), this);
            if (mainInstance != null) {
                mainInstance.clientDisconnected(this);
            }
        } finally {
            if (mainInstance != null) {
                mainInstance.removeClient(this);
            }
            try {
                socket.socket().close();
                socket.close();
                Log.write(Level.INFO, "XmlClient", ((mainInstance == null) ? "" : mainInstance.getInstanceId()), Messages.getString("ThreadStopped"), this);
            } catch (Exception ex) {
            }
            if (UipServer.getUipsInstance().getUipsStatus() != UipsStatus.Stopped && UipServer.getUipsInstance().getUipsStatus() != UipsStatus.Unknown) {
                if (mainInstance != null) {
                    DatabaseAccess.notifyInstanceStatus(mainInstance.getInstanceId());
                }
                DatabaseAccess.notifyServerStatus();
            }
        }
    }
