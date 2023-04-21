    @Override
    public String getNext(InkTracePoint sp) {
        return (sp.get(getChannel().getName()) > 0.5) ? "T" : "F";
    }
