    @Override
    public void appendCommandDetail(final StringBuilder b) {
        for (int i = 0; i < changes.length; i++) {
            b.append(" ");
            b.append(changes[i].getChannelId());
            b.append("[");
            b.append(changes[i].getDmxValue());
            b.append("]");
        }
        b.append('\n');
    }
