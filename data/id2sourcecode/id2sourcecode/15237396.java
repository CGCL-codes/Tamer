    private static void attachChildren(final Mixer mixer, MixerNode node, HashMap validOutCache) {
        for (int i = 0; i < mixer.getChannels().size(); i++) {
            Channel c = mixer.getChannels().getChannel(i);
            if (c.getOutChannel().equals(node.name)) {
                MixerNode m = new MixerNode();
                m.name = c.getName();
                m.channel = c;
                configureNode(m, validOutCache);
                node.children.add(m);
            }
        }
        ArrayList temp = new ArrayList();
        for (int i = 0; i < mixer.getSubChannels().size(); i++) {
            Channel c = mixer.getSubChannels().getChannel(i);
            if (c.getOutChannel().equals(node.name)) {
                MixerNode m = new MixerNode();
                m.name = c.getName();
                m.channel = c;
                configureNode(m, validOutCache);
                temp.add(m);
                attachChildren(mixer, m, validOutCache);
            }
        }
        if (temp.size() > 0) {
            Collections.sort(temp, new Comparator() {

                public int compare(Object o1, Object o2) {
                    MixerNode node1 = (MixerNode) o1;
                    MixerNode node2 = (MixerNode) o2;
                    if (mixer.sendsTo(node1.channel, node2.channel)) {
                        return -1;
                    } else if (mixer.sendsTo(node2.channel, node1.channel)) {
                        return 1;
                    }
                    return 0;
                }
            });
            node.children.addAll(temp);
        }
    }
