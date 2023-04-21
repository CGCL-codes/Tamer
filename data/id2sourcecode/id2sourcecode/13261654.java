    public List getChannels() {
        Vector eventSets = new Vector();
        for (Enumeration enumeration = channels.elements(); enumeration.hasMoreElements(); ) {
            eventSets.add(enumeration.nextElement());
        }
        return eventSets;
    }
