    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getClass().getName());
        b.append("(");
        b.append(dimmer.getId());
        b.append("=>");
        b.append(dimmer.getChannelId());
        b.append(")");
        String string = b.toString();
        return string;
    }
