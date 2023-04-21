    private void insertStripIntoGrid(Strip newStrip) {
        if (newStrip == null) {
            System.err.println("Null strip insert attempt, ignoring.");
            return;
        } else if (activeStripSet.contains(newStrip)) {
            System.err.println("Duplicate strip insert: " + newStrip + ", ignoring.");
            return;
        } else if (!newStrip.isActive() || !newStrip.isBound()) {
            System.err.println("Strip: " + newStrip + " is not yet bound.  Ignoring.");
            return;
        }
        SplitAxis stripAxis = lrd.getStripAxis();
        Comparator<Strip> cr = new Comparator<Strip>() {

            private List<Strip> stripList = getStripList();

            public int compare(Strip o1, Strip o2) {
                Integer o1pos = stripList.indexOf(o1);
                Integer o2pos = stripList.indexOf(o2);
                return o1pos.compareTo(o2pos);
            }
        };
        if (this.activeStripSet.size() == 0) {
            final SplitLine newline = stripAxis.getMinStuckLine();
            newline.setRowObject(newStrip);
            newStrip.setMinLine(newline);
        } else {
            SplitLine newLine = new SplitLine();
            final SplitLine minStuckLine = stripAxis.getMinStuckLine();
            final Strip minStuckStrip = (Strip) minStuckLine.getRowObject();
            if (cr.compare(newStrip, minStuckStrip) < 0) {
                newLine.setRowObject(minStuckStrip);
                minStuckStrip.setMinLine(newLine);
                minStuckLine.setRowObject(newStrip);
                newStrip.setMinLine(minStuckLine);
                stripAxis.putAt(newLine, minStuckLine);
            } else {
                newLine.setRowObject(newStrip);
                newStrip.setMinLine(newLine);
                final SplitLine adjSplit = findLeftAdjacentStripSplit(stripAxis, newStrip, cr);
                stripAxis.putAt(newLine, adjSplit);
            }
        }
        activeStripSet.add(newStrip);
        List<InputChannelItemInterface> stripChannels = newStrip.getChannels();
        for (Iterator<InputChannelItemInterface> channelIt = stripChannels.iterator(); channelIt.hasNext(); ) {
            final InputChannelItemInterface channel = channelIt.next();
            Set<Strip> stripSet = inputChannelItemToStripSet.get(channel);
            if (stripSet == null) {
                stripSet = new HashSet<Strip>(5);
                inputChannelItemToStripSet.put(channel, stripSet);
            }
            stripSet.add(newStrip);
        }
    }
