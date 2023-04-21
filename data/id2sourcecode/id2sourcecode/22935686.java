    public String getNext(InkTracePoint sp) {
        if (getChannel().isIntermittent()) {
            return prepair(valueOf(sp.get(getChannel().getName())));
        } else {
            return getNext(sp.get(getChannel().getName()));
        }
    }
