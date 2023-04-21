    public String toXML(String namespace) {
        String pre = (namespace == null) ? "" : namespace + ":";
        StringBuffer buff = new StringBuffer();
        buff.append("<" + pre + "channelUseDescription>\n");
        outThing(buff, pre, "channelName", this.getChannelName());
        outThing(buff, pre, "channelDesc", this.getChannelDesc());
        outThing(buff, pre, "messagesPerHour", this.getMessagesPerHour());
        outThing(buff, pre, "kBytesPerHour", this.getKBytesPerHour());
        outThing(buff, pre, "leaseLengthSeconds", this.getLeaseLengthSeconds());
        if (this.receiveEvents != null) {
            buff.append("<" + pre + "receiveEvents>");
            for (Iterator<EventDescription> ii = receiveEvents.iterator(); ii.hasNext(); ) {
                buff.append(ii.next().toXML(namespace));
            }
            buff.append("</" + pre + "receiveEvents>");
        }
        if (this.sendEvents != null) {
            buff.append("<" + pre + "sendEvents>");
            for (Iterator<EventDescription> ii = sendEvents.iterator(); ii.hasNext(); ) {
                buff.append(ii.next().toXML(namespace));
            }
            buff.append("</" + pre + "sendEvents>");
        }
        buff.append("</" + pre + "channelUseDescription>\n");
        return buff.toString();
    }
