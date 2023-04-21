    private void exploreAdjacency(List<FaultModeSet> faultSets, List<String> destination, Set<String> destination_set, List<String> arcs, List<Map<String, FaultModeSet>> componentBindings, boolean collatedbailout, String arcbailout, int numberDone, int pof) {
        if (isInterrupted()) {
            finish();
            return;
        }
        if (destination.size() > maxPathLength) {
            maxPathLengthReachedFlag = true;
            return;
        }
        String lastDestination = destination.get(destination.size() - 1).toString();
        int columnCount = lifting.getAdjacencyMatrix().getNumberOfColumns();
        boolean isSink = true;
        boolean viableReason = false;
        for (int c = 0; c < columnCount; c++) {
            String proposedStep = lifting.getAdjacencyMatrix().getColumnName(c);
            if (debug) {
                System.out.println(destination + "--?-->" + proposedStep);
            }
            if (destination_set.contains(proposedStep)) {
                continue;
            }
            FaultModeSet fss = lifting.getAdjacencyMatrix().getValueAt(lifting.getAdjacencyMatrix().getRowIndex(lastDestination), c);
            if (fss.isEmpty()) {
                continue;
            }
            Map<String, FaultModeSet> newComponentBindings = componentBindings.get(componentBindings.size() - 1);
            String association = "", name = "", description = "", instance = "";
            for (FaultMode faultMode : fss.getAllFaultModes()) {
                association = faultMode.getAssociation();
                name = faultMode.getAbbreviation();
                description = faultMode.getDescription();
                instance = faultMode.getAssociatedInstance();
                break;
            }
            String componentassociation = (instance == null ? "" : instance) + "::" + association;
            boolean isAnArc = association.startsWith(LiftingVisitor.CONNECTIONON);
            boolean isCollated = false;
            if (!isAnArc) {
                if (!lifting.getComponentDefinition(association).get(0).getComponentModel().isTransitive()) {
                    if (collatedbailout) {
                        viableReason = true;
                        if (debug) {
                            System.out.println(proposedStep + " not considered (collated)");
                        }
                        continue;
                    }
                    isCollated = true;
                }
                if (componentBindings.get(componentBindings.size() - 1).containsKey(componentassociation)) {
                    fss = componentBindings.get(componentBindings.size() - 1).get(componentassociation).intersect(fss);
                }
                if (fss.isEmpty()) {
                    viableReason = true;
                    if (debug) {
                        System.out.println(proposedStep + " not considered (incompatible modes)");
                        System.out.println(componentBindings);
                    }
                    continue;
                }
                newComponentBindings = new TreeMap<String, FaultModeSet>(componentBindings.get(componentBindings.size() - 1));
                if (newComponentBindings.containsKey(componentassociation)) {
                    newComponentBindings.remove(componentassociation);
                }
                newComponentBindings.put(componentassociation, fss);
                pof = 0;
                double max_path_prob = 0.0;
                for (FaultModeSet fs : newComponentBindings.values()) {
                    boolean normalFound = false;
                    double max_prob_fault_set = 0.0;
                    for (FaultMode fm : fs.getAllFaultModes()) {
                        if (fm.isNormal()) {
                            normalFound = true;
                        }
                        if (fm.getProbability() > max_prob_fault_set) {
                            max_prob_fault_set = fm.getProbability();
                        }
                    }
                    max_path_prob *= max_prob_fault_set;
                    if (!normalFound) {
                        if (++pof > pofLimit) {
                            break;
                        }
                    } else {
                        max_path_prob = 1.0;
                        if (max_path_prob < probabilityLowerBound) {
                            break;
                        }
                    }
                }
                if (pof > pofLimit) {
                    viableReason = true;
                    if (debug) {
                        System.out.println(proposedStep + " not considered (too many faults)");
                    }
                    continue;
                }
                if (max_path_prob < probabilityLowerBound) {
                    viableReason = true;
                    if (debug) {
                        System.out.print(proposedStep + " not considered (lowest min probability reached) ");
                        System.out.println(max_path_prob + " < " + probabilityLowerBound);
                    }
                    continue;
                }
            } else {
                if (!followArcDupes && arcbailout != null && name.equals(arcbailout)) {
                    viableReason = true;
                    if (debug) {
                        System.out.println(proposedStep + " not considered (arc duplication)");
                    }
                    continue;
                }
                int idx = description.indexOf(LiftingVisitor.INDEXCODE);
                if (idx >= 0) {
                    int lookupCode = Integer.parseInt(description.substring(idx + LiftingVisitor.INDEXCODE.length()));
                    if (lifting.lookupArc(lookupCode).getChannelInformation() == DataFlow.NO_DATA) {
                        viableReason = true;
                        if (debug) {
                            System.out.println(proposedStep + " not considered (not a data channel)");
                        }
                        continue;
                    }
                }
            }
            if (getAllRedSources().contains(proposedStep)) {
                viableReason = true;
                if (debug) {
                    System.out.println(proposedStep + " not considered (red source encountered)");
                }
                continue;
            }
            if (getAllBlackSinks().contains(proposedStep)) {
                Iterator<FaultModeSet> fsItr = faultSets.iterator();
                Iterator<String> destItr = destination.iterator();
                Iterator<Map<String, FaultModeSet>> bindingsItr = componentBindings.iterator();
                if (pathTrees[pof] == null) {
                    pathTrees[pof] = new DefaultMutableTreeNode();
                    portTrees[pof] = new DefaultMutableTreeNode();
                    resultTreeSizes[pof] = 0;
                }
                DefaultMutableTreeNode displayPointer = pathTrees[pof];
                DefaultMutableTreeNode portPointer = portTrees[pof];
                while (fsItr.hasNext()) {
                    FaultModeSet fsnext = fsItr.next();
                    String dest = destItr.next();
                    Map<String, FaultModeSet> bindings = bindingsItr.next();
                    Enumeration<?> displayChildren = displayPointer.children();
                    Enumeration<?> portChildren = portPointer.children();
                    DefaultMutableTreeNode displayFuturePointer = null;
                    DefaultMutableTreeNode portFuturePointer = null;
                    StepDescription stepDesc = new StepDescription(fsnext, dest);
                    stepDesc.setBindings(bindings);
                    while (displayChildren.hasMoreElements()) {
                        DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) displayChildren.nextElement();
                        if (dmtn.getUserObject().equals(stepDesc)) {
                            displayFuturePointer = dmtn;
                            break;
                        }
                    }
                    while (portChildren.hasMoreElements()) {
                        DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) portChildren.nextElement();
                        if (dmtn.getUserObject().equals(dest)) {
                            portFuturePointer = dmtn;
                            break;
                        }
                    }
                    if (displayFuturePointer == null) {
                        displayFuturePointer = new DefaultMutableTreeNode();
                        displayFuturePointer.setUserObject(stepDesc);
                        displayPointer.add(displayFuturePointer);
                    }
                    if (portFuturePointer == null) {
                        portFuturePointer = new DefaultMutableTreeNode();
                        portFuturePointer.setUserObject(dest);
                        portPointer.add(portFuturePointer);
                    }
                    displayPointer = displayFuturePointer;
                    portPointer = portFuturePointer;
                }
                StepDescription finalStep = new StepDescription(fss, proposedStep);
                finalStep.setBindings(newComponentBindings);
                displayPointer.add(new DefaultMutableTreeNode(finalStep));
                portPointer.add(new DefaultMutableTreeNode(proposedStep));
                resultTreeSizes[pof]++;
                ++numPathsFound;
                if (progress != null) progress.note(progressNotePrefix + " - " + numPathsFound + " paths found.");
                if (debug) {
                    System.out.println(" PATH FOUND -> " + proposedStep);
                }
                if (arcTrees[pof] == null) {
                    arcTrees[pof] = new ArcTree(null, "root");
                }
                arcTrees[pof].addPath(arcs);
                if (findOnePathOnly) {
                    return;
                }
                continue;
            }
            List<String> newDestination = new ArrayList<String>(destination);
            Set<String> newDestination_set = new TreeSet<String>(destination_set);
            List<FaultModeSet> newFaultSets = new ArrayList<FaultModeSet>(faultSets);
            List<String> newArcs = new ArrayList<String>(arcs);
            newDestination.add(proposedStep);
            newDestination_set.add(proposedStep);
            newFaultSets.add(fss);
            List<Map<String, FaultModeSet>> newComponentBindingsList = new LinkedList<Map<String, FaultModeSet>>(componentBindings);
            newComponentBindingsList.add(newComponentBindings);
            if (isAnArc) {
                newArcs.add(name + " from " + lastDestination + description);
            }
            isSink = false;
            exploreAdjacency(newFaultSets, newDestination, newDestination_set, newArcs, newComponentBindingsList, isCollated, isAnArc ? name : null, -1, pof);
        }
        if (debug && isSink && !viableReason) {
            System.out.println(destination);
            System.out.println("** SINK **");
            System.out.println(componentBindings);
        }
    }
