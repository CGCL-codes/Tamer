    private final void addManeuversRestrictionsFromMNshpAndMPdbf() throws Exception {
        FileChannel in = new FileInputStream(this.mpDbfFileName).getChannel();
        DbaseFileReader r = new DbaseFileReader(in, true);
        int mpIdNameIndex = -1;
        int mpSeqNrNameIndex = -1;
        int mpTrpelIDNameIndex = -1;
        for (int i = 0; i < r.getHeader().getNumFields(); i++) {
            if (r.getHeader().getFieldName(i).equals(mpIdName)) {
                mpIdNameIndex = i;
            }
            if (r.getHeader().getFieldName(i).equals(mpSeqNrName)) {
                mpSeqNrNameIndex = i;
            }
            if (r.getHeader().getFieldName(i).equals(mpTrpelIDName)) {
                mpTrpelIDNameIndex = i;
            }
        }
        if (mpIdNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + srIdName + "' not found.");
        }
        if (mpSeqNrNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + srSpeedName + "' not found.");
        }
        if (mpTrpelIDNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + srValDirName + "' not found.");
        }
        log.debug("  FieldName-->Index:");
        log.debug("    " + mpIdName + "-->" + mpIdNameIndex);
        log.debug("    " + mpSeqNrName + "-->" + mpSeqNrNameIndex);
        log.debug("    " + mpTrpelIDName + "-->" + mpTrpelIDNameIndex);
        log.info("  parsing meneuver paths dbf file...");
        TreeMap<Id, TreeMap<Integer, Id>> mSequences = new TreeMap<Id, TreeMap<Integer, Id>>();
        while (r.hasNext()) {
            Object[] entries = r.readEntry();
            Id mpId = new IdImpl(entries[mpIdNameIndex].toString());
            int mpSeqNr = Integer.parseInt(entries[mpSeqNrNameIndex].toString());
            Id linkId = new IdImpl(entries[mpTrpelIDNameIndex].toString());
            TreeMap<Integer, Id> mSequence = mSequences.get(mpId);
            if (mSequence == null) {
                mSequence = new TreeMap<Integer, Id>();
            }
            if (mSequence.put(mpSeqNr, linkId) != null) {
                throw new IllegalArgumentException(mpIdName + "=" + mpId + ": " + mpSeqNrName + " " + mpSeqNr + " already exists.");
            }
            mSequences.put(mpId, mSequence);
        }
        log.info("    " + mSequences.size() + " maneuvers sequences stored.");
        log.info("  done.");
        log.info("  parsing meneuver shape file...");
        TreeMap<Id, ArrayList<Tuple<Id, Integer>>> maneuvers = new TreeMap<Id, ArrayList<Tuple<Id, Integer>>>();
        FeatureSource fs = ShapeFileReader.readDataFile(this.mnShpFileName);
        for (Object o : fs.getFeatures()) {
            Feature f = (Feature) o;
            int featType = Integer.parseInt(f.getAttribute(mnFeatTypeName).toString());
            if ((featType == 2103) || (featType == 2102) || (featType == 2101)) {
                Id nodeId = new IdImpl(f.getAttribute(mnJnctIdName).toString());
                ArrayList<Tuple<Id, Integer>> ms = maneuvers.get(nodeId);
                if (ms == null) {
                    ms = new ArrayList<Tuple<Id, Integer>>();
                }
                Tuple<Id, Integer> m = new Tuple<Id, Integer>(new IdImpl(f.getAttribute(mnIdName).toString()), featType);
                ms.add(m);
                maneuvers.put(nodeId, ms);
            } else if ((featType == 9401) || (featType == 2104)) {
            } else {
                throw new IllegalArgumentException("mnId=" + f.getAttribute(mnIdName) + ": " + mnFeatTypeName + "=" + featType + " not known.");
            }
        }
        log.info("    " + maneuvers.size() + " nodes with maneuvers stored.");
        log.info("  done.");
        log.info("  expand nodes according to the given manveuvers...");
        int nodesIgnoredCnt = 0;
        int nodesAssignedCnt = 0;
        int maneuverIgnoredCnt = 0;
        int maneuverAssignedCnt = 0;
        int virtualNodesCnt = 0;
        int virtualLinksCnt = 0;
        for (Id nodeId : maneuvers.keySet()) {
            if (network.getNode(nodeId) == null) {
                log.debug("  nodeid=" + nodeId + ": maneuvers exist for that node but node is missing. Ignoring and proceeding anyway...");
                nodesIgnoredCnt++;
            } else {
                Node n = network.getNode(nodeId);
                TreeMap<Id, TreeMap<Id, Boolean>> mmatrix = new TreeMap<Id, TreeMap<Id, Boolean>>();
                ArrayList<Tuple<Id, Integer>> ms = maneuvers.get(nodeId);
                for (int i = 0; i < ms.size(); i++) {
                    Tuple<Id, Integer> m = ms.get(i);
                    TreeMap<Integer, Id> mSequence = mSequences.get(m.getFirst());
                    if (mSequence == null) {
                        throw new Exception("nodeid=" + nodeId + "; mnId=" + m.getFirst() + ": no maneuver sequence given.");
                    }
                    if (mSequence.size() < 2) {
                        throw new Exception("nodeid=" + nodeId + "; mnId=" + m.getFirst() + ": mSequenceSize=" + mSequence.size() + " not alowed!");
                    }
                    Iterator<Integer> snr_it = mSequence.keySet().iterator();
                    Integer snr = snr_it.next();
                    while (snr_it.hasNext()) {
                        Integer snr2 = snr_it.next();
                        Link inLink = n.getInLinks().get(new IdImpl(mSequence.get(snr) + "FT"));
                        if (inLink == null) {
                            inLink = n.getInLinks().get(new IdImpl(mSequence.get(snr) + "TF"));
                        }
                        Link outLink = n.getOutLinks().get(new IdImpl(mSequence.get(snr2) + "FT"));
                        if (outLink == null) {
                            outLink = n.getOutLinks().get(new IdImpl(mSequence.get(snr2) + "TF"));
                        }
                        if ((inLink != null) && (outLink != null)) {
                            if (m.getSecond() == 2102) {
                                TreeMap<Id, Boolean> outLinkMap = mmatrix.get(inLink.getId());
                                if (outLinkMap == null) {
                                    outLinkMap = new TreeMap<Id, Boolean>();
                                }
                                outLinkMap.put(outLink.getId(), true);
                                mmatrix.put(inLink.getId(), outLinkMap);
                            } else {
                                TreeMap<Id, Boolean> outLinkMap = mmatrix.get(inLink.getId());
                                if (outLinkMap == null) {
                                    outLinkMap = new TreeMap<Id, Boolean>();
                                }
                                outLinkMap.put(outLink.getId(), false);
                                mmatrix.put(inLink.getId(), outLinkMap);
                            }
                            maneuverAssignedCnt++;
                        } else {
                            maneuverIgnoredCnt++;
                        }
                    }
                }
                for (Id fromLinkId : mmatrix.keySet()) {
                    boolean hasRestrictedManeuver = false;
                    for (Id toLinkId : mmatrix.get(fromLinkId).keySet()) {
                        Boolean b = mmatrix.get(fromLinkId).get(toLinkId);
                        if (b) {
                            hasRestrictedManeuver = true;
                        }
                    }
                    for (Id toLinkId : n.getOutLinks().keySet()) {
                        if (!mmatrix.get(fromLinkId).containsKey(toLinkId)) {
                            if (hasRestrictedManeuver) {
                                mmatrix.get(fromLinkId).put(toLinkId, false);
                            } else {
                                mmatrix.get(fromLinkId).put(toLinkId, true);
                            }
                        }
                    }
                }
                for (Id fromLinkId : n.getInLinks().keySet()) {
                    if (!mmatrix.containsKey(fromLinkId)) {
                        mmatrix.put(fromLinkId, new TreeMap<Id, Boolean>());
                        for (Id toLinkId : n.getOutLinks().keySet()) {
                            mmatrix.get(fromLinkId).put(toLinkId, true);
                        }
                    }
                }
                if (this.removeUTurns) {
                    for (Id fromLinkId : n.getInLinks().keySet()) {
                        String str1 = fromLinkId.toString().substring(0, fromLinkId.toString().length() - 2);
                        for (Id toLinkId : n.getOutLinks().keySet()) {
                            String str2 = toLinkId.toString().substring(0, toLinkId.toString().length() - 2);
                            if (str1.equals(str2)) {
                                mmatrix.get(fromLinkId).put(toLinkId, false);
                            }
                        }
                    }
                }
                ArrayList<Tuple<Id, Id>> turns = new ArrayList<Tuple<Id, Id>>();
                for (Id fromLinkId : mmatrix.keySet()) {
                    for (Id toLinkId : mmatrix.get(fromLinkId).keySet()) {
                        Boolean b = mmatrix.get(fromLinkId).get(toLinkId);
                        if (b) {
                            turns.add(new Tuple<Id, Id>(fromLinkId, toLinkId));
                        }
                    }
                }
                Tuple<ArrayList<Node>, ArrayList<Link>> t = expandNode(network, nodeId, turns, expansionRadius, linkSeparation);
                virtualNodesCnt += t.getFirst().size();
                virtualLinksCnt += t.getSecond().size();
                nodesAssignedCnt++;
            }
        }
        log.info("    " + nodesAssignedCnt + " nodes expanded.");
        log.info("    " + maneuverAssignedCnt + " maneuvers assigned.");
        log.info("    " + virtualNodesCnt + " new nodes created.");
        log.info("    " + virtualLinksCnt + " new links created.");
        log.info("    " + nodesIgnoredCnt + " nodes with given maneuvers (2103, 2102 or 2101) ignored.");
        log.info("    " + maneuverIgnoredCnt + " maneuvers ignored (while node was found).");
        log.info("  done.");
    }
