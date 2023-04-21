    public static void load(InputStream is, Turingmachine res, MachineTree machTree) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setValidating(true);
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        docBuilder.setErrorHandler(new ErrorHandler() {

            public void warning(SAXParseException arg0) throws SAXException {
                throw arg0;
            }

            public void error(SAXParseException arg0) throws SAXException {
                throw arg0;
            }

            public void fatalError(SAXParseException arg0) throws SAXException {
                throw arg0;
            }
        });
        Document doc = docBuilder.parse(is, (new File(Constants.MACHINE_DTD_FILE)).toURI().toString());
        HashMap<Integer, Integer> idMap = new HashMap<Integer, Integer>();
        Element root = doc.getDocumentElement();
        Node child = root.getFirstChild();
        while (child != null) {
            if (child.getNodeName().equals("description")) {
                res.setDescription(child.getFirstChild().getNodeValue());
            } else if (child.getNodeName().equals("alphabet")) {
                Vector<String> tmp = getStringValuesOutOfNode(child, "symbol");
                for (String it : tmp) res.addSymbols(it);
            } else if (child.getNodeName().equals("state")) {
                String shortName = null;
                String posX = null;
                String posY = null;
                String stateID = null;
                String kind = null;
                stateID = child.getAttributes().getNamedItem("stateID").getNodeValue();
                kind = child.getAttributes().getNamedItem("kind").getNodeValue();
                Node subChild = child.getFirstChild();
                while (subChild != null) {
                    if (subChild.getNodeName().equals("shortName")) {
                        shortName = subChild.getFirstChild().getNodeValue();
                    } else if (subChild.getNodeName().equals("position")) {
                        posX = getStringValuesOutOfNode(subChild, "posX").elementAt(0);
                        posY = getStringValuesOutOfNode(subChild, "posY").elementAt(0);
                    }
                    subChild = subChild.getNextSibling();
                }
                StateType st = null;
                if (kind.equals(Constants.STATE_A_XML_STRING)) st = new StateType(false, true); else if (kind.equals(Constants.STATE_SA_XML_STRING)) st = new StateType(true, true); else if (kind.equals(Constants.STATE_N_XML_STRING)) st = new StateType(false, false); else if (kind.equals(Constants.STATE_S_XML_STRING)) st = new StateType(true, false);
                int curID = res.addState(shortName, st, new Point(Integer.parseInt(posX), Integer.parseInt(posY)));
                idMap.put(Integer.valueOf(Integer.parseInt(stateID.substring(1))), Integer.valueOf(curID));
            } else if (child.getNodeName().equals("submachine")) {
                String shortName = null;
                String posX = null;
                String posY = null;
                String submachineID = null;
                String path = null;
                submachineID = child.getAttributes().getNamedItem("submachineID").getNodeValue();
                Node subChild = child.getFirstChild();
                while (subChild != null) {
                    if (subChild.getNodeName().equals("shortName")) {
                        shortName = subChild.getFirstChild().getNodeValue();
                    } else if (subChild.getNodeName().equals("position")) {
                        posX = getStringValuesOutOfNode(subChild, "posX").elementAt(0);
                        posY = getStringValuesOutOfNode(subChild, "posY").elementAt(0);
                    } else if (subChild.getNodeName().equals("path")) {
                        path = subChild.getFirstChild().getNodeValue();
                    }
                    subChild = subChild.getNextSibling();
                }
                Turingmachine tm;
                tm = machTree.getMachineByFilePath(Config.makeAbsolute(path));
                int curID = res.addSubmachine(shortName, new Point(Integer.parseInt(posX), Integer.parseInt(posY)), tm);
                idMap.put(Integer.valueOf(Integer.parseInt(submachineID.substring(1))), Integer.valueOf(curID));
            } else if (child.getNodeName().equals("trans")) {
                Vector<TransitionOpns> opns = new Vector<TransitionOpns>();
                Vector<Point> dots = new Vector<Point>();
                String startName = null;
                String endName = null;
                String anchorExpr = null;
                Point anchor = null;
                String toID = null;
                String fromID = null;
                toID = child.getAttributes().getNamedItem("to").getNodeValue();
                fromID = child.getAttributes().getNamedItem("from").getNodeValue();
                Node subChild = child.getFirstChild();
                while (subChild != null) {
                    if (subChild.getNodeName().equals("startName")) {
                        startName = subChild.getFirstChild().getNodeValue();
                    } else if (subChild.getNodeName().equals("endName")) {
                        endName = subChild.getFirstChild().getNodeValue();
                    } else if (subChild.getNodeName().equals("anchorExpr")) {
                        anchorExpr = subChild.getFirstChild().getNodeValue();
                    } else if (subChild.getNodeName().equals("position")) {
                        String posX = null;
                        String posY = null;
                        posX = getStringValuesOutOfNode(subChild, "posX").elementAt(0);
                        posY = getStringValuesOutOfNode(subChild, "posY").elementAt(0);
                        dots.add(new Point(Integer.parseInt(posX), Integer.parseInt(posY)));
                    } else if (subChild.getNodeName().equals("positionAnchor")) {
                        String posX = null;
                        String posY = null;
                        posX = getStringValuesOutOfNode(subChild, "posX").elementAt(0);
                        posY = getStringValuesOutOfNode(subChild, "posY").elementAt(0);
                        anchor = new Point(Integer.parseInt(posX), Integer.parseInt(posY));
                    } else if (subChild.getNodeName().equals("operation")) {
                        String tapeID = null;
                        String headMove = null;
                        String symbolRead = null;
                        String symbolWrite = null;
                        tapeID = subChild.getAttributes().getNamedItem("tapeID").getNodeValue();
                        headMove = subChild.getAttributes().getNamedItem("headMove").getNodeValue();
                        Node subSubChild = subChild.getFirstChild();
                        while (subSubChild != null) {
                            if (subSubChild.getNodeName().equals("symbolRead")) {
                                symbolRead = subSubChild.getFirstChild().getNodeValue();
                            } else if (subSubChild.getNodeName().equals("symbolWrite")) {
                                symbolWrite = subSubChild.getFirstChild().getNodeValue();
                            }
                            subSubChild = subSubChild.getNextSibling();
                        }
                        HeadMove hm = null;
                        if (headMove.equals(Constants.HEAD_NONE_STRING_SHORT)) hm = new HeadMove(HeadMove.direction.NONE); else if (headMove.equals(Constants.HEAD_LEFT_STRING_SHORT)) hm = new HeadMove(HeadMove.direction.LEFT); else if (headMove.equals(Constants.HEAD_RIGHT_STRING_SHORT)) hm = new HeadMove(HeadMove.direction.RIGHT);
                        Tape t = res.getTapeByID(idMap.get(new Integer(Integer.parseInt(tapeID.substring(1)))).intValue());
                        Symbol read = res.getSymbol(symbolRead);
                        if (read == null && symbolRead.equals(Constants.XML_BLANK)) read = new Symbol(Symbol.symbolType.BLANK); else if (read == null && symbolRead.equals(Constants.XML_EPSILON)) read = new Symbol(Symbol.symbolType.EPSILON);
                        Symbol write = res.getSymbol(symbolWrite);
                        if (write == null && symbolRead.equals(Constants.XML_BLANK)) write = new Symbol(Symbol.symbolType.BLANK); else if (write == null && symbolRead.equals(Constants.XML_EPSILON)) write = new Symbol(Symbol.symbolType.EPSILON);
                        opns.add(new TransitionOpns(hm, read, write, t));
                    }
                    subChild = subChild.getNextSibling();
                }
                int realFromID = idMap.get(new Integer(Integer.parseInt(fromID.substring(1)))).intValue();
                int realToID = idMap.get(new Integer(Integer.parseInt(toID.substring(1)))).intValue();
                Transition tran = new Transition(res.getOval(realFromID), startName, res.getOval(realToID), endName, dots, opns, anchor, res);
                res.addTransition(tran);
                res.setAnchorExpr(tran, anchorExpr);
            } else if (child.getNodeName().equals("tape")) {
                String tapeID = null;
                String tapeDescription = null;
                tapeID = child.getAttributes().getNamedItem("tapeID").getNodeValue();
                Vector<String> itemsStr = new Vector<String>();
                Vector<Integer> itemsInt = new Vector<Integer>();
                Node subChild = child.getFirstChild();
                while (subChild != null) {
                    if (subChild.getNodeName().equals("description")) {
                        tapeDescription = subChild.getFirstChild().getNodeValue();
                    } else if (subChild.getNodeName().equals("symbPos")) {
                        Node subSubChild = subChild.getFirstChild();
                        String symb = null;
                        String pos = null;
                        while (subSubChild != null) {
                            if (subSubChild.getNodeName().equals("symbol")) {
                                symb = subSubChild.getFirstChild().getNodeValue();
                            } else if (subSubChild.getNodeName().equals("pos")) {
                                pos = subSubChild.getFirstChild().getNodeValue();
                            }
                            subSubChild = subSubChild.getNextSibling();
                        }
                        itemsStr.add(symb);
                        itemsInt.add(new Integer(Integer.parseInt(pos)));
                    }
                    subChild = subChild.getNextSibling();
                }
                int parsedTapeID = Integer.parseInt(tapeID.substring(1));
                int realTapeID = res.addTape(tapeDescription).getID();
                idMap.put(Integer.valueOf(parsedTapeID), Integer.valueOf(realTapeID));
                Iterator itS = itemsStr.iterator();
                Iterator itI = itemsInt.iterator();
                for (; itS.hasNext() && itI.hasNext(); ) res.setSymbolAt(res.getTapeByID(realTapeID), (String) itS.next(), ((Integer) itI.next()).intValue());
            }
            child = child.getNextSibling();
        }
    }
