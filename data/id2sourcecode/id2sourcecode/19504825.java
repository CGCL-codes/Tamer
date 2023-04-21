    private void reportOnConnection(BeamAnalyzerBean babean) {
        if (!getChannelCorrelator().isNoBadBPMs()) {
            ArrayList bad = getChannelCorrelator().getBadBPMs();
            Iterator iter = bad.iterator();
            ArrayList names = new ArrayList(bad.size());
            while (iter.hasNext()) {
                String name = (String) iter.next();
                if (name.endsWith("xAvg")) {
                    name = name.replaceAll(":xAvg", "");
                    if (!names.contains(name)) names.add(name);
                } else if (name.endsWith("yAvg")) {
                    name = name.replaceAll(":yAvg", "");
                    if (!names.contains(name)) names.add(name);
                }
            }
            if (getChannelCorrelator().getGoodBPMs().isEmpty()) {
                getOrbitDisplay().getReportArea().append("SORRY! Could not connect to any of the BPM in selected sequence.\n");
                babean.removeNotConnectableBPMs(names);
                return;
            }
            babean.removeNotConnectableBPMs(names);
            Iterator it = names.iterator();
            getOrbitDisplay().getReportArea().append("Could not connect to following BPMs :\n");
            while (it.hasNext()) {
                getOrbitDisplay().getReportArea().append(" - " + (String) it.next() + "\n");
            }
        } else babean.setConnectableBPMs();
    }
