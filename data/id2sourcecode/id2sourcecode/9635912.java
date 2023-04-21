    @FromState("Started")
    @OnMessage(performative = "QueryRef")
    @PossibleOutcome("ChannelTransfer")
    public WorkflowState onQueryRef(Message msg) throws Exception {
        String kind = getPart(msg, CONTENT, 0);
        assert kind != null : new NullPointerException("missing query-ref kind");
        HGHandle proc = getPart(msg, CONTENT, 1);
        assert proc != null : new NullPointerException("missing processor handle");
        if ("channels-of".equals(kind)) {
            List<HGHandle> channelLinks = hg.findAll(getThisPeer().getGraph(), hg.and(hg.type(ChannelLink.class), hg.incident(proc)));
            List<Object> channels = new ArrayList<Object>();
            for (HGHandle ch : channelLinks) {
                ChannelLink<?> theLink = getThisPeer().getGraph().get(ch);
                System.out.println("Transfer link " + theLink.getChannel());
                channels.add(SubgraphManager.getTransferAtomRepresentation(getThisPeer().getGraph(), ch));
            }
            reply(msg, Performative.InformRef, channels);
            return ChannelTransfer;
        } else {
            reply(msg, Performative.NotUnderstood, "unrecognized query-ref kind");
            return WorkflowState.Failed;
        }
    }
