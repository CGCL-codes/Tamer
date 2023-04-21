    public Coherence(SharedDataContainer SDC, String sharedSpaceName) {
        this.dataPath = (String) SDC.getMetadata().get(Metadata.PATH);
        this.SDC = SDC;
        this.neighbours = new ArrayList<Member>();
        System.out.println("Coherence = " + sharedSpaceName + "Data path = " + this.dataPath);
        CoherenceCommunication cc = CoherenceCommunication.instance(sharedSpaceName);
        cc.register(this, dataPath);
        this.chan = CoherenceCommunication.instance(sharedSpaceName).getChannel();
        this.myself = CoherenceCommunication.instance(sharedSpaceName).getMyself();
        this.owner = (String) SDC.getMetadata().get(Metadata.OWNER);
        this.isOwner = owner.equals(this.myself.getKey().toString());
    }
