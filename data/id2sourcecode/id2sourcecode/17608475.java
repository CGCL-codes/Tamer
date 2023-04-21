    public Object getValueAt(final Object node, final int column) {
        Object value = "";
        if (node instanceof Fixture) {
            Fixture fixture = (Fixture) node;
            switch(column) {
                case 0:
                    value = fixture.getName();
            }
        } else if (node instanceof Attribute) {
            Attribute attribute = (Attribute) node;
            switch(column) {
                case 0:
                    value = attribute.getDefinition().getName();
                    break;
                case 1:
                    value = attribute;
                    break;
                case 2:
                    StringBuilder b = new StringBuilder();
                    for (int i = 0; i < attribute.getChannelCount(); i++) {
                        FixtureChannel channel = attribute.getChannel(i);
                        int channelIndex = channel.getNumber() - 1;
                        Buffer buffer = getContext().getLanbox().getMixer();
                        float f = buffer.getLevels().get(channelIndex).getValue();
                        int dmx = Dmx.getDmxValue(f);
                        if (i > 0) {
                            b.append(',');
                        }
                        b.append(dmx);
                    }
                    value = b.toString();
                    break;
            }
        }
        return value;
    }
