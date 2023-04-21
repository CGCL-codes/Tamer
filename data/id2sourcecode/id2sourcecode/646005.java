    public int addEPGmatches(StringBuffer buff, int format) throws Exception {
        DataStore store = DataStore.getInstance();
        int num = 0;
        SimpleDateFormat df = new SimpleDateFormat("EE d HH:mm");
        int type = 0;
        try {
            type = Integer.parseInt(store.getProperty("Capture.deftype"));
        } catch (Exception e) {
        }
        conflictList.clear();
        CaptureDeviceList devList = CaptureDeviceList.getInstance();
        EpgMatch epgMatch = null;
        Calendar cal = Calendar.getInstance();
        Vector channelMap = getChannelMap();
        Set wsChannels = store.getChannels().keySet();
        if (channelMap == null || channelMap.size() == 0) {
            if (format == 1) buff.append("Channel map not set so could not run Auto-Add system.<br>\n"); else buff.append("Channel map not set so could not run Auto-Add system.\n");
            System.out.println("Channel map not set so could not run Auto-Add system.");
            return 0;
        }
        if (format == 1) buff.append("About to run Auto-Add system.<br><br>\n"); else buff.append("About to run Auto-Add system.\n\n");
        GuideItem[] progs = null;
        GuideItem guidItem = null;
        ScheduleItem schItem = null;
        int conflictNum = 0;
        Vector epgMatchList = store.getEpgMatchList();
        System.out.println("");
        for (int x = 0; x < epgMatchList.size(); x++) {
            epgMatch = (EpgMatch) epgMatchList.get(x);
            if (epgMatch.isEnabled()) {
                for (int z = 0; z < channelMap.size(); z++) {
                    String[] chanMap = (String[]) channelMap.get(z);
                    if (wsChannels.contains(chanMap[0])) {
                        progs = getProgramsForChannel(chanMap[1]);
                        for (int y = 0; y < progs.length; y++) {
                            guidItem = progs[y];
                            boolean foundMatch = false;
                            Vector matchNames = epgMatch.getMatchListNames();
                            HashMap matchLists = store.getMatchLists();
                            EpgMatchList matcher = null;
                            for (int nameIndex = 0; nameIndex < matchNames.size(); nameIndex++) {
                                String matchListName = (String) matchNames.get(nameIndex);
                                matcher = (EpgMatchList) matchLists.get(matchListName);
                                if (matcher != null) {
                                    if (matcher.isMatch(guidItem, chanMap[0])) {
                                        foundMatch = true;
                                        break;
                                    }
                                }
                            }
                            if (foundMatch && guidItem.getIgnored() == true) {
                                String itemAction = "IGNORED";
                                String itemResult = "Item Ignored";
                                String itemDetails = guidItem.getName() + " " + df.format(guidItem.getStart());
                                if (format == 1) {
                                    buff.append("<b><font color='#FF0000'>" + itemAction + "</font></b> - " + itemDetails + " (" + itemResult + ")<br>\n");
                                } else buff.append(itemAction + " - " + itemDetails + " (" + itemResult + ")\n");
                            } else if (foundMatch) {
                                schItem = new ScheduleItem(guidItem, chanMap[0], type, store.rand.nextLong(), epgMatch.getAutoDel());
                                String[] patterns = store.getNamePatterns();
                                schItem.setFilePattern(patterns[0]);
                                schItem.setKeepFor(epgMatch.getKeepFor());
                                schItem.setPostTask(epgMatch.getPostTask());
                                schItem.setFilePattern(epgMatch.GetFileNamePattern());
                                schItem.setCapType(epgMatch.getCaptureType());
                                schItem.setCapturePathIndex(epgMatch.getCapturePathIndex());
                                cal.setTime(schItem.getStart());
                                cal.add(Calendar.MINUTE, (epgMatch.getStartBuffer() * -1));
                                schItem.setDuration(guidItem.getDuration() + epgMatch.getStartBuffer() + epgMatch.getEndBuffer());
                                schItem.setStart(cal);
                                String itemDetails = schItem.getName() + " (" + df.format(schItem.getStart(), new StringBuffer(), new FieldPosition(0)) + " " + schItem.getDuration() + " " + schItem.getChannel() + ") ";
                                System.out.println("Matched Item = " + schItem.getName() + " " + schItem.getStart() + " " + schItem);
                                String itemAction = "";
                                StringBuffer conflictResult = new StringBuffer();
                                String itemResult = "";
                                boolean alreadyInList = isAlreadyInList(schItem, epgMatch.getExistingCheckType());
                                if (alreadyInList) {
                                    itemAction = "ERROR";
                                    itemResult = "Already Exists";
                                    AddConflictDetails acd = new AddConflictDetails(guidItem.getName());
                                    acd.setConflict("A schedule for this EPG item already Exists.");
                                    acd.setReason("Already Exists");
                                    conflictList.add(acd);
                                } else if (cal.after(Calendar.getInstance())) {
                                    int overlaps = numOverlaps(schItem, conflictResult);
                                    if (overlaps <= devList.getDeviceCount()) {
                                        store.addScheduleItem(schItem);
                                        itemAction = "ADDED";
                                        num++;
                                    } else {
                                        itemAction = "ERROR";
                                        itemResult = "Item overlapped";
                                        AddConflictDetails acd = new AddConflictDetails(itemDetails);
                                        acd.setConflict(conflictResult.toString());
                                        acd.setReason(itemResult);
                                        conflictList.add(acd);
                                        conflictNum++;
                                    }
                                } else {
                                    itemAction = "ERROR";
                                    itemResult = "Before now";
                                }
                                if (format == 1) {
                                    if (itemAction.equalsIgnoreCase("ERROR")) buff.append("<b><font color='#FF0000'>" + itemAction + "</font></b> - " + itemDetails + " (" + itemResult + ")<br>\n"); else buff.append("<b><font color='#00FF00'>" + itemAction + "</font></b> - " + itemDetails + " (" + itemResult + ")<br>\n");
                                } else buff.append(itemAction + " - " + itemDetails + " (" + itemResult + ")\n");
                            }
                        }
                    }
                }
            } else {
                Vector items = epgMatch.getMatchListNames();
                String names = "";
                for (int q = 0; q < items.size(); q++) {
                    String name = (String) items.get(q);
                    if (format == 1) {
                        name = name.replaceAll("<", "&lt;");
                        name = name.replaceAll(">", "&gt;");
                    }
                    if (q == items.size() - 1) {
                        names += name;
                    } else names += name + ", ";
                }
                if (names.length() == 0) names = "No Match List Assigned";
                if (format == 1) {
                    buff.append("Auto-Add (" + names + ") is disabled.<br>\n");
                } else {
                    buff.append("Auto-Add (" + names + ") is disabled.\n");
                }
            }
        }
        if (format == 1) buff.append("<br>Matched and added " + num + " items from the EPG data.<br>\n"); else buff.append("\nMatched and added " + num + " items from the EPG data.\n");
        if (conflictNum > 0 && format == 1) {
            buff.append("<br>There were " + conflictNum + " conflicts, check the <a href='/servlet/EpgAutoAddDataRes?action=25'>CONFLICT REPORT PAGE</a> for more info.<br>\n");
        }
        return num;
    }
