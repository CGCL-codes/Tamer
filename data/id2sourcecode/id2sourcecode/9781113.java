    public Collection getReferences() {
        Collection list = new ArrayList(reads.size() + writes.size());
        list.addAll(reads);
        list.addAll(writes);
        return list;
    }
