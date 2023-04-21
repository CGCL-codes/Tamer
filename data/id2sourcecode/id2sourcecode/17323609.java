    public static void writeDOT(ChannelMonitor monitor, OutputStream os) throws IOException {
        if (monitor == null || os == null) {
            return;
        }
        Map<String, String> actorMap = new HashMap<String, String>();
        os.write("digraph channels {\n".getBytes(ENCODING));
        os.write("  graph [center rankdir=LR];\n".getBytes(ENCODING));
        os.write("  node [fontsize=10];\n".getBytes(ENCODING));
        int channelId = 1;
        for (Iterator<MonitoredChannel> iterator = monitor.getChannels(); iterator.hasNext(); ) {
            final MonitoredChannel channel = iterator.next();
            String fill = channel.isPoisoned() ? "fillcolor=\"lightblue2\", style=\"filled\"" : "fillcolor=\"lightgray\", style=\"filled\"";
            String depth = channel.isBuffered() ? String.format(" {%d/%d}", channel.size(), channel.getBufferCapacity()) : "";
            os.write(String.format("  node [shape=\"box\" %s label=\"%s%s\"] channel_%d;\n", fill, channel.getName(), depth, channelId).getBytes(ENCODING));
            for (Iterator<MonitoredPort> iterator2 = channel.getWritePorts(); iterator2.hasNext(); ) {
                final MonitoredPort port = iterator2.next();
                final String actorsName = port.getOwningActor().getName();
                final String style = port.isClosed() ? "[style=\"dashed\"]" : "[style=\"solid\"]";
                if (actorMap.containsKey(actorsName)) {
                    final String actorName = actorMap.get(actorsName);
                    os.write(String.format("  %s -> channel_%d %s;\n", actorName, channelId, style).getBytes(ENCODING));
                } else {
                    os.write(String.format("  node [shape=\"ellipse\" fillcolor=\"lightgray\", style=\"filled\" label=\"%s\"] actor_%d;\n", actorsName, port.getOwningActor().getLocalId()).getBytes(ENCODING));
                    final String actorName = String.format("actor_%d", port.getOwningActor().getLocalId());
                    actorMap.put(actorsName, actorName);
                    os.write(String.format("  %s -> channel_%d %s;\n", actorName, channelId, style).getBytes(ENCODING));
                }
            }
            for (Iterator<MonitoredPort> iterator2 = channel.getReadPorts(); iterator2.hasNext(); ) {
                final MonitoredPort port = iterator2.next();
                if (port.getOwningActor() != null) {
                    final String actorsName = port.getOwningActor().getName();
                    final String style = port.isClosed() ? "[style=\"dashed\"]" : "[style=\"solid\"]";
                    if (actorMap.containsKey(actorsName)) {
                        final String actorName = actorMap.get(actorsName);
                        os.write(String.format("  channel_%d -> %s %s;\n", channelId, actorName, style).getBytes(ENCODING));
                    } else {
                        os.write(String.format("  node [shape=\"ellipse\" fillcolor=\"lightgray\", style=\"filled\" label=\"%s\"] actor_%d;\n", actorsName, port.getOwningActor().getLocalId()).getBytes(ENCODING));
                        final String actorName = String.format("actor_%d", port.getOwningActor().getLocalId());
                        actorMap.put(actorsName, actorName);
                        os.write(String.format("  channel_%d -> %s %s;\n", channelId, actorName, style).getBytes(ENCODING));
                    }
                }
            }
            channelId++;
        }
        os.write("}\n".getBytes(ENCODING));
    }
