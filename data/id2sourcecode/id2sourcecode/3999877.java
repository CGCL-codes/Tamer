    @SuppressWarnings("unchecked")
    public static Element stateMachineDiagramToPetriModule(Element stateMachineDiagram, boolean allModules) {
        Element pnmlNodes = null;
        Element itSettingsNode = null;
        Element module = null;
        pnmlNodes = (Element) ItSIMPLE.getCommonData().getChild("petriNetNodes").getChild("pnmlNodes").clone();
        itSettingsNode = ItSIMPLE.getItSettings();
        String version = "";
        if (pnmlNodes != null) {
            module = (Element) pnmlNodes.getChild("moduleNodes").getChild("module").clone();
            if (itSettingsNode.getChild("version") != null) {
                version = itSettingsNode.getChildText("version");
            }
            String classID = stateMachineDiagram.getChildText("class");
            module.setAttribute("name", "M" + classID);
            List<Element> firstActionEnds = null;
            HashSet<String> initialStateIDs = new HashSet<String>();
            try {
                XPath path = new JDOMXPath("associations/action/associationEnds/actionEnd[@element='initialState']");
                firstActionEnds = path.selectNodes(stateMachineDiagram);
            } catch (JaxenException e) {
                e.printStackTrace();
            }
            for (Iterator<Element> iter = firstActionEnds.iterator(); iter.hasNext(); ) {
                Element firstActionEnd = iter.next();
                if (firstActionEnd != null) {
                    Element associationEnds = firstActionEnd.getParentElement();
                    Element secondActionEnd = null;
                    try {
                        XPath path = new JDOMXPath("actionEnd[@id='2']");
                        secondActionEnd = (Element) path.selectSingleNode(associationEnds);
                    } catch (JaxenException e) {
                        e.printStackTrace();
                    }
                    if (secondActionEnd != null) {
                        initialStateIDs.add(secondActionEnd.getAttributeValue("element-id"));
                    }
                }
            }
            List<Element> states = stateMachineDiagram.getChild("states").getChildren("state");
            int placeNumberID = 0;
            for (Iterator<Element> statesIter = states.iterator(); statesIter.hasNext(); ) {
                Element state = statesIter.next();
                Element place = (Element) pnmlNodes.getChild("place").clone();
                place.setAttribute("id", "p" + placeNumberID);
                place.getChild("name").getChild("text").setText(state.getChildText("name"));
                int stateX = Integer.parseInt(state.getChild("graphics").getChild("position").getAttributeValue("x"));
                int stateY = Integer.parseInt(state.getChild("graphics").getChild("position").getAttributeValue("y"));
                int stateWidth = Integer.parseInt(state.getChild("graphics").getChild("size").getAttributeValue("width"));
                int stateHeight = Integer.parseInt(state.getChild("graphics").getChild("size").getAttributeValue("height"));
                int x = stateX + stateWidth / 2;
                int y = stateY + stateHeight / 2 + currentY;
                place.getChild("graphics").getChild("position").setAttribute("x", Integer.toString(x - 19));
                place.getChild("graphics").getChild("position").setAttribute("y", Integer.toString(y - 19));
                if (y > maxY) {
                    maxY = y;
                }
                place.getChild("toolspecific").setAttribute("version", version);
                place.getChild("toolspecific").getChild("state").setText(state.getAttributeValue("id"));
                place.getChild("toolspecific").getChild("class").setText(stateMachineDiagram.getChildText("class"));
                if (initialStateIDs.contains(state.getAttributeValue("id"))) {
                    place.getChild("initialMarking").getChild("text").setText("1");
                }
                module.addContent(place);
                placeNumberID++;
            }
            List<Element> actions = stateMachineDiagram.getChild("associations").getChildren("action");
            LinkedList<Element> arcList = new LinkedList<Element>();
            int arcNumberID = 0;
            int transitionNumberID = 0;
            for (Iterator<Element> actionsIter = actions.iterator(); actionsIter.hasNext(); ) {
                Element action = actionsIter.next();
                Element transition = null;
                Element operator = null;
                Element operatorClass = null;
                try {
                    XPath path = new JDOMXPath("project/elements/classes/class[@id='" + action.getChild("reference").getAttributeValue("class") + "']" + "/operators/operator[@id='" + action.getChild("reference").getAttributeValue("operator") + "']");
                    operator = (Element) path.selectSingleNode(project.getDocument());
                } catch (JaxenException e) {
                    e.printStackTrace();
                }
                String transitionID = "";
                String transitionName = "";
                if (operator != null) {
                    operatorClass = operator.getParentElement().getParentElement();
                    transitionID = "t" + transitionNumberID;
                    transitionName = "";
                    if (!operatorClass.getAttributeValue("id").equals(classID)) {
                        transitionName = operatorClass.getChildText("name") + "::" + operator.getChildText("name");
                        Element moduleInterface = (Element) module.getChild("interface");
                        Element importTransition = (Element) pnmlNodes.getChild("moduleNodes").getChild("importTransition").clone();
                        importTransition.setAttribute("id", "cl" + operatorClass.getAttributeValue("id") + "op" + operator.getAttributeValue("id"));
                        moduleInterface.addContent(importTransition);
                        transition = (Element) pnmlNodes.getChild("referenceTransition").clone();
                        transition.setAttribute("ref", "cl" + operatorClass.getAttributeValue("id") + "op" + operator.getAttributeValue("id"));
                        transition.getChild("toolspecific").getChild("type").setText("import");
                    } else {
                        List<Element> exportedActions = null;
                        try {
                            XPath path = new JDOMXPath("project/diagrams/stateMachineDiagrams/stateMachineDiagram[@id!=" + stateMachineDiagram.getAttributeValue("id") + "]/associations/action[reference/@class=" + classID + " and reference/@operator=" + operator.getAttributeValue("id") + "]");
                            exportedActions = path.selectNodes(project.getDocument());
                        } catch (JaxenException e) {
                            e.printStackTrace();
                        }
                        if (exportedActions.size() > 0) {
                            transitionName = operator.getChildText("name");
                            Element moduleInterface = (Element) module.getChild("interface");
                            Element exportTransition = (Element) pnmlNodes.getChild("moduleNodes").getChild("exportTransition").clone();
                            exportTransition.setAttribute("id", "cl" + operatorClass.getAttributeValue("id") + "op" + operator.getAttributeValue("id"));
                            exportTransition.setAttribute("ref", transitionID);
                            moduleInterface.addContent(exportTransition);
                            transition = (Element) pnmlNodes.getChild("transition").clone();
                            if (!allModules) transition.getChild("toolspecific").getChild("type").setText("export");
                        } else {
                            transitionName = operator.getChildText("name");
                            transition = (Element) pnmlNodes.getChild("transition").clone();
                            transitionID = "t" + transitionNumberID;
                        }
                    }
                    transition.getChild("toolspecific").setAttribute("version", version);
                    transition.getChild("toolspecific").getChild("class").setText(operatorClass.getAttributeValue("id"));
                    transition.getChild("toolspecific").getChild("operator").setText(operator.getAttributeValue("id"));
                    transition.getChild("name").getChild("text").setText(transitionName);
                } else {
                    transition = (Element) pnmlNodes.getChild("transition").clone();
                    transitionID = "t" + transitionNumberID;
                    transition.getChild("toolspecific").setAttribute("version", version);
                    transition.getChild("toolspecific").removeChild("class");
                    transition.getChild("toolspecific").removeChild("operator");
                    transition.getChild("toolspecific").removeChild("preconditionGroup");
                }
                transition.setAttribute("id", transitionID);
                List<Element> actionPoints = action.getChild("graphics").getChild("points").getChildren("point");
                List<Element> actionEnds = null;
                try {
                    XPath path = new JDOMXPath("associationEnds/actionEnd");
                    actionEnds = path.selectNodes(action);
                } catch (JaxenException e) {
                    e.printStackTrace();
                }
                String sourceID = null;
                String targetID = null;
                for (Iterator<Element> associationIter = actionEnds.iterator(); associationIter.hasNext(); ) {
                    Element actionEnd = associationIter.next();
                    if (actionEnd.getAttributeValue("navigation").equals("false")) {
                        sourceID = actionEnd.getAttributeValue("element-id");
                    } else if (actionEnd.getAttributeValue("navigation").equals("true")) {
                        targetID = actionEnd.getAttributeValue("element-id");
                    }
                }
                Element sourceState = null;
                try {
                    XPath path = new JDOMXPath("states/state[@id='" + sourceID + "']");
                    sourceState = (Element) path.selectSingleNode(stateMachineDiagram);
                } catch (JaxenException e) {
                    e.printStackTrace();
                }
                Element targetState = null;
                try {
                    XPath path = new JDOMXPath("states/state[@id='" + targetID + "']");
                    targetState = (Element) path.selectSingleNode(stateMachineDiagram);
                } catch (JaxenException e) {
                    e.printStackTrace();
                }
                if (sourceState != null && targetState != null) {
                    int x = 0;
                    int y = 0;
                    int actionPointsSize = actionPoints.size();
                    if (actionPointsSize == 0) {
                        int stateX = Integer.parseInt(sourceState.getChild("graphics").getChild("position").getAttributeValue("x"));
                        int stateY = Integer.parseInt(sourceState.getChild("graphics").getChild("position").getAttributeValue("y"));
                        int stateWidth = Integer.parseInt(sourceState.getChild("graphics").getChild("size").getAttributeValue("width"));
                        int stateHeight = Integer.parseInt(sourceState.getChild("graphics").getChild("size").getAttributeValue("height"));
                        int xPositionSource = stateX + stateWidth / 2;
                        int yPositionSource = stateY + stateHeight / 2;
                        stateX = Integer.parseInt(targetState.getChild("graphics").getChild("position").getAttributeValue("x"));
                        stateY = Integer.parseInt(targetState.getChild("graphics").getChild("position").getAttributeValue("y"));
                        stateWidth = Integer.parseInt(targetState.getChild("graphics").getChild("size").getAttributeValue("width"));
                        stateHeight = Integer.parseInt(targetState.getChild("graphics").getChild("size").getAttributeValue("height"));
                        int xPositionTarget = stateX + stateWidth / 2;
                        int yPositionTarget = stateY + stateHeight / 2;
                        x = (xPositionSource + xPositionTarget) / 2;
                        y = currentY + (yPositionSource + yPositionTarget) / 2;
                        if (y > maxY) {
                            maxY = y;
                        }
                    } else if (actionPointsSize % 2 == 0) {
                        Element pointOne = actionPoints.get(actionPointsSize / 2 - 1);
                        Element pointTwo = actionPoints.get(actionPointsSize / 2);
                        int xPositionOne = Integer.parseInt(pointOne.getAttributeValue("x"));
                        int xPositionTwo = Integer.parseInt(pointTwo.getAttributeValue("x"));
                        int yPositionOne = Integer.parseInt(pointOne.getAttributeValue("y"));
                        int yPositionTwo = Integer.parseInt(pointTwo.getAttributeValue("y"));
                        x = (xPositionOne + xPositionTwo) / 2;
                        y = currentY + (yPositionOne + yPositionTwo) / 2;
                        if (y > maxY) {
                            maxY = y;
                        }
                    } else if (actionPointsSize % 2 != 0) {
                        Element point = actionPoints.get(actionPointsSize / 2);
                        x = Integer.parseInt(point.getAttributeValue("x"));
                        y = currentY + Integer.parseInt(point.getAttributeValue("y"));
                        if (y > maxY) {
                            maxY = y;
                        }
                    }
                    transition.getChild("graphics").getChild("position").setAttribute("x", Integer.toString(x - 12));
                    transition.getChild("graphics").getChild("position").setAttribute("y", Integer.toString(y - 19));
                    module.addContent(transition);
                    transitionNumberID++;
                    Element sourcePlace = null;
                    try {
                        XPath path = new JDOMXPath("place[toolspecific/class='" + classID + "' and toolspecific/state='" + sourceState.getAttributeValue("id") + "']");
                        sourcePlace = (Element) path.selectSingleNode(module);
                    } catch (JaxenException e) {
                        e.printStackTrace();
                    }
                    Element targetPlace = null;
                    try {
                        XPath path = new JDOMXPath("place[toolspecific/class='" + classID + "' and toolspecific/state='" + targetState.getAttributeValue("id") + "']");
                        targetPlace = (Element) path.selectSingleNode(module);
                    } catch (JaxenException e) {
                        e.printStackTrace();
                    }
                    Element arc1 = (Element) pnmlNodes.getChild("arc").clone();
                    arc1.setAttribute("id", "arc" + Integer.toString(arcNumberID));
                    arc1.setAttribute("source", sourcePlace.getAttributeValue("id"));
                    arc1.setAttribute("target", transition.getAttributeValue("id"));
                    arc1.getChild("type").setAttribute("value", "normal");
                    arcNumberID = arcNumberID + 1;
                    Element arc2 = (Element) pnmlNodes.getChild("arc").clone();
                    arc2.setAttribute("id", "arc" + Integer.toString(arcNumberID));
                    arc2.setAttribute("source", transition.getAttributeValue("id"));
                    arc2.setAttribute("target", targetPlace.getAttributeValue("id"));
                    arc2.getChild("type").setAttribute("value", "normal");
                    if (actionPointsSize > 0) {
                        if (actionPointsSize % 2 == 0) {
                            for (int i = 0; i < actionPointsSize; i++) {
                                Element point = actionPoints.get(i);
                                Element position = (Element) pnmlNodes.getChild("position").clone();
                                int positionX = Integer.parseInt(point.getAttributeValue("x"));
                                int positionY = currentY + Integer.parseInt(point.getAttributeValue("y"));
                                if (positionY > maxY) {
                                    maxY = positionY;
                                }
                                position.setAttribute("x", Integer.toString(positionX));
                                position.setAttribute("y", Integer.toString(positionY));
                                if (i < actionPointsSize / 2) {
                                    arc1.getChild("graphics").addContent(position);
                                } else {
                                    arc2.getChild("graphics").addContent(position);
                                }
                            }
                        }
                    }
                    arcList.add(arc1);
                    arcList.add(arc2);
                    arcNumberID++;
                }
            }
            for (Iterator<Element> arcIter = arcList.iterator(); arcIter.hasNext(); ) {
                Element currentArc = arcIter.next();
                module.addContent(currentArc);
            }
            currentY = maxY + MODULE_OFFSET;
        }
        return module;
    }
