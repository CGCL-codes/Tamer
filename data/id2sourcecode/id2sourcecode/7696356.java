    public synchronized void removedMTP(MTPEvent ev) {
        Channel ch = ev.getChannel();
        ContainerID cid = ev.getPlace();
        String proto = ch.getProtocol();
        String address = ch.getAddress();
        RemovedMTP rmtp = new RemovedMTP();
        rmtp.setAddress(address);
        rmtp.setProto(proto);
        rmtp.setWhere(cid);
        EventRecord er = new EventRecord(rmtp, localContainer);
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
