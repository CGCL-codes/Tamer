    public void fillAttributes(Set readSet, Set writeSet, MDWfState state) throws Exception {
        if (getState() == state) {
            readSet.add(attr);
            if (canWrite) writeSet.add(attr);
        }
    }
