    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Collection<MemberImpl> getMember(@QueryParam("channel") String channel) {
        Collection<MemberImpl> list = new ArrayList<MemberImpl>();
        for (Member m : Backyard.channelhandler.getChannel(channel).getMembers()) {
            list.add((MemberImpl) m);
        }
        return list;
    }
