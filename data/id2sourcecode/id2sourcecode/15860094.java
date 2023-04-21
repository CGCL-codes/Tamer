    public MemoryReferee makePhysicalComponent(List readList, List writeList) {
        if (this.doSimpleArbiter) {
            this.referee = new SimpleMemoryReferee(this, readList, writeList);
        } else {
            this.referee = new MemoryReferee(this, readList, writeList);
        }
        return referee;
    }
