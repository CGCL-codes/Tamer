    public synchronized void addedMTP(MTPEvent ev) {
        Channel ch = ev.getChannel();
        ContainerID cid = ev.getPlace();
        String proto = ch.getProtocol();
        String address = ch.getAddress();
        AddedMTP amtp = new AddedMTP();
        amtp.setAddress(address);
        amtp.setProto(proto);
        amtp.setWhere(cid);
        EventRecord er = new EventRecord(amtp, localContainer);
        er.setWhen(ev.getTime());
        eventQueue.put(er);
        if (theAms != null) {
            PlatformDescription ap = new PlatformDescription();
            ap.setPlatform(theAms.getDescriptionAction(null));
            er = new EventRecord(ap, localContainer);
            er.setWhen(ev.getTime());
            eventQueue.put(er);
        }
    }
