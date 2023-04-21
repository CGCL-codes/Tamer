    @Override
    public void appendCommandDetail(final StringBuilder b) {
        for (int i = 0; i < parameters.size(); i++) {
            b.append(" ");
            b.append(parameters.getDimmerId(i) + 1);
            b.append("[");
            b.append(parameters.getChannelId(i) + 1);
            b.append("]");
        }
        b.append('\n');
    }
