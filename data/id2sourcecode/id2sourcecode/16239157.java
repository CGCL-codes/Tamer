    private void run2(final Network network) throws Exception {
        log.info("running " + this.getClass().getName() + " module...");
        NetworkExpandNode neModule = new NetworkExpandNode(network, expansionRadius, this.linkSeparation);
        FileChannel in = new FileInputStream(this.mpDbfFileName).getChannel();
        DbaseFileReader r = new DbaseFileReader(in, true);
        int mpIdNameIndex = -1;
        int mpSeqNrNameIndex = -1;
        int mpTrpelIDNameIndex = -1;
        for (int i = 0; i < r.getHeader().getNumFields(); i++) {
            if (r.getHeader().getFieldName(i).equals(MP_ID_NAME)) {
                mpIdNameIndex = i;
            }
            if (r.getHeader().getFieldName(i).equals(MP_SEQNR_NAME)) {
                mpSeqNrNameIndex = i;
            }
            if (r.getHeader().getFieldName(i).equals(MP_TRPELID_NAME)) {
                mpTrpelIDNameIndex = i;
            }
        }
        if (mpIdNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + MP_ID_NAME + "' not found.");
        }
        if (mpSeqNrNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + MP_SEQNR_NAME + "' not found.");
        }
        if (mpTrpelIDNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + MP_TRPELID_NAME + "' not found.");
        }
        log.trace("  FieldName-->Index:");
        log.trace("    " + MP_ID_NAME + "-->" + mpIdNameIndex);
        log.trace("    " + MP_SEQNR_NAME + "-->" + mpSeqNrNameIndex);
        log.trace("    " + MP_TRPELID_NAME + "-->" + mpTrpelIDNameIndex);
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
                throw new IllegalArgumentException(MP_ID_NAME + "=" + mpId + ": " + MP_SEQNR_NAME + " " + mpSeqNr + " already exists.");
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
            int featType = Integer.parseInt(f.getAttribute(MN_FEATTYP_NAME).toString());
            if ((featType == 2103) || (featType == 2102) || (featType == 2101)) {
                Id nodeId = new IdImpl(f.getAttribute(MN_JNCTID_NAME).toString());
                ArrayList<Tuple<Id, Integer>> ms = maneuvers.get(nodeId);
                if (ms == null) {
                    ms = new ArrayList<Tuple<Id, Integer>>();
                }
                Tuple<Id, Integer> m = new Tuple<Id, Integer>(new IdImpl(f.getAttribute(MN_ID_NAME).toString()), featType);
                ms.add(m);
                maneuvers.put(nodeId, ms);
            } else if ((featType == 9401) || (featType == 2104)) {
            } else {
                throw new IllegalArgumentException("mnId=" + f.getAttribute(MN_ID_NAME) + ": " + MN_FEATTYP_NAME + "=" + featType + " not known.");
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
        for (Map.Entry<Id, ArrayList<Tuple<Id, Integer>>> entry : maneuvers.entrySet()) {
            Id nodeId = entry.getKey();
            if (network.getNodes().get(nodeId) == null) {
                log.trace("  nodeid=" + nodeId + ": maneuvers exist for that node but node is missing. Ignoring and proceeding anyway...");
                nodesIgnoredCnt++;
            } else {
                Node n = network.getNodes().get(nodeId);
                TreeMap<Id, TreeMap<Id, Boolean>> mmatrix = new TreeMap<Id, TreeMap<Id, Boolean>>();
                ArrayList<Tuple<Id, Integer>> ms = entry.getValue();
                for (Tuple<Id, Integer> m : ms) {
                    TreeMap<Integer, Id> mSequence = mSequences.get(m.getFirst());
                    if (mSequence == null) {
                        throw new Exception("nodeid=" + nodeId + "; mnId=" + m.getFirst() + ": no maneuver sequence given.");
                    }
                    if (mSequence.size() < 2) {
                        throw new Exception("nodeid=" + nodeId + "; mnId=" + m.getFirst() + ": mSequenceSize=" + mSequence.size() + " not alowed!");
                    }
                    Id firstLinkid = mSequence.values().iterator().next();
                    for (Id otherLinkId : mSequence.values()) {
                        Link inLink = n.getInLinks().get(new IdImpl(firstLinkid + "FT"));
                        if (inLink == null) {
                            inLink = n.getInLinks().get(new IdImpl(firstLinkid + "TF"));
                        }
                        Link outLink = n.getOutLinks().get(new IdImpl(otherLinkId + "FT"));
                        if (outLink == null) {
                            outLink = n.getOutLinks().get(new IdImpl(otherLinkId + "TF"));
                        }
                        if ((inLink != null) && (outLink != null)) {
                            if (m.getSecond() == 2102) {
                                TreeMap<Id, Boolean> outLinkMap = mmatrix.get(inLink.getId());
                                if (outLinkMap == null) {
                                    outLinkMap = new TreeMap<Id, Boolean>();
                                }
                                outLinkMap.put(outLink.getId(), Boolean.TRUE);
                                mmatrix.put(inLink.getId(), outLinkMap);
                            } else {
                                TreeMap<Id, Boolean> outLinkMap = mmatrix.get(inLink.getId());
                                if (outLinkMap == null) {
                                    outLinkMap = new TreeMap<Id, Boolean>();
                                }
                                outLinkMap.put(outLink.getId(), Boolean.FALSE);
                                mmatrix.put(inLink.getId(), outLinkMap);
                            }
                            maneuverAssignedCnt++;
                        } else {
                            maneuverIgnoredCnt++;
                        }
                    }
                }
                for (TreeMap<Id, Boolean> fromLinkEntry : mmatrix.values()) {
                    boolean hasRestrictedManeuver = false;
                    for (Boolean b : fromLinkEntry.values()) {
                        if (b.booleanValue()) {
                            hasRestrictedManeuver = true;
                        }
                    }
                    for (Id toLinkId : n.getOutLinks().keySet()) {
                        if (!fromLinkEntry.containsKey(toLinkId)) {
                            fromLinkEntry.put(toLinkId, Boolean.valueOf(!hasRestrictedManeuver));
                        }
                    }
                }
                for (Id fromLinkId : n.getInLinks().keySet()) {
                    if (!mmatrix.containsKey(fromLinkId)) {
                        mmatrix.put(fromLinkId, new TreeMap<Id, Boolean>());
                        for (Id toLinkId : n.getOutLinks().keySet()) {
                            mmatrix.get(fromLinkId).put(toLinkId, Boolean.TRUE);
                        }
                    }
                }
                if (this.removeUTurns) {
                    for (Id fromLinkId : n.getInLinks().keySet()) {
                        String str1 = fromLinkId.toString().substring(0, fromLinkId.toString().length() - 2);
                        for (Id toLinkId : n.getOutLinks().keySet()) {
                            String str2 = toLinkId.toString().substring(0, toLinkId.toString().length() - 2);
                            if (str1.equals(str2)) {
                                mmatrix.get(fromLinkId).put(toLinkId, Boolean.FALSE);
                            }
                        }
                    }
                }
                ArrayList<TurnInfo> turns = new ArrayList<TurnInfo>();
                for (Map.Entry<Id, TreeMap<Id, Boolean>> fromLinkEntry : mmatrix.entrySet()) {
                    Id fromLinkId = fromLinkEntry.getKey();
                    for (Map.Entry<Id, Boolean> toLinkEntry : fromLinkEntry.getValue().entrySet()) {
                        if (toLinkEntry.getValue().booleanValue()) {
                            turns.add(new TurnInfo(fromLinkId, toLinkEntry.getKey()));
                        }
                    }
                }
                Tuple<List<Node>, List<Link>> t = neModule.expandNode(nodeId, turns);
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
        log.info("done.");
    }
