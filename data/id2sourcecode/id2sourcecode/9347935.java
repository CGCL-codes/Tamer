    private final TextureGraphNode setTexNode(TextureGraphNode o, TextureGraphNode n) {
        if (o != null) o.getChannel().removeChannelChangeListener(this);
        if (n == null) return null;
        n.getChannel().addChannelChangeListener(this);
        return n;
    }
