    private int occuranceCounter(DepthFirstPathsAnalysis.ArcTree atree) {
        int totalChildCount = 1;
        Enumeration<?> ii = atree.children();
        while (ii.hasMoreElements()) {
            DepthFirstPathsAnalysis.ArcTree child = (DepthFirstPathsAnalysis.ArcTree) ii.nextElement();
            totalChildCount += occuranceCounter(child);
            atreeIndex.put(new Integer(child.getLookup()), child);
        }
        Integer lookup = new Integer(atree.getLookup());
        if (lifting.lookupArc(lookup).getChannelInformation() != DataFlow.CONFIRMED_DATA) {
            Integer count = occurances.get(lookup);
            if (count == null) {
                count = new Integer(0);
            } else {
                occurances.remove(lookup);
            }
            count = new Integer(count + totalChildCount);
            occurances.put(lookup, count);
        }
        return totalChildCount;
    }
