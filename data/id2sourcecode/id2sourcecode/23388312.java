    public String toString() {
        final StringBuilder out = new StringBuilder(64);
        out.append("(");
        for (int i = 0; i < CHANNEL_NAMES.length; i++) if (getChannel(i)) {
            if (out.length() > 1) out.append(',');
            out.append(CHANNEL_NAMES[i]);
            out.append("=True");
        }
        out.append(")");
        return out.toString();
    }
