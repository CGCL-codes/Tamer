    private void handleRequest(ContactRequestEvent ev) {
        ev.unserializeFromMessage();
        List<Peer> list = knownPeers.get(ev.getTargetOverLay());
        int numContacts = ev.getNumberOfContacts();
        int listSize = list.size();
        List<Peer> subList = null;
        if (list != null) {
            if (listSize > numContacts) subList = new ArrayList<Peer>(list.subList(listSize - numContacts, listSize)); else {
                subList = new ArrayList<Peer>(list);
            }
            subList.remove(ev.getRequesterID());
        } else subList = new ArrayList<Peer>();
        try {
            ContactReplyEvent reply = new ContactReplyEvent(ev.getTargetOverLay(), subList, ev.getChannel(), Direction.invert(ev.getDir()), this);
            reply.serializeToMessage();
            reply.dest = ev.source;
            reply.go();
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
    }
