    public static void writeRDF(ChannelMonitor monitor, OutputStream os, String format) throws IOException {
        if (monitor == null || os == null || format == null) {
            return;
        }
        for (Iterator<MonitoredChannel> iterator = monitor.getChannels(); iterator.hasNext(); ) {
            MonitoredChannel channel = iterator.next();
            final String name = channel.getName();
            int portId = 1;
            os.write(String.format("<%s> a <http://acpj.googlecode.com/vocab#Channel> .\n", name).getBytes(ENCODING));
            os.write(String.format("<%s> <http://acpj.googlecode.com/vocab#isBuffered> \"%s\" .\n", name, channel.isBuffered()).getBytes(ENCODING));
            if (channel.isBuffered()) {
                os.write(String.format("<%s> <http://acpj.googlecode.com/vocab#capacity> \"%d\" .\n", name, channel.getBufferCapacity()).getBytes(ENCODING));
                os.write(String.format("<%s> <http://acpj.googlecode.com/vocab#size> \"%d\" .\n", name, channel.size()).getBytes(ENCODING));
            }
            os.write(String.format("<%s> <http://acpj.googlecode.com/vocab#readPortArity> \"%s\" .\n", name, channel.getReadPortArity()).getBytes(ENCODING));
            for (Iterator<MonitoredPort> iterator2 = channel.getReadPorts(); iterator2.hasNext(); ) {
                MonitoredPort port = iterator2.next();
                os.write(String.format("<%s> <http://acpj.googlecode.com/vocab#hasPort> _:P%02d .\n", name, portId).getBytes(ENCODING));
                os.write(String.format("_:P%02d a <http://acpj.googlecode.com/vocab#ReadPort> .\n", portId).getBytes(ENCODING));
                os.write(String.format("_:P%02d <http://acpj.googlecode.com/vocab#owningActor> <%s> .\n", portId, port.getOwningActor().getName()).getBytes(ENCODING));
                os.write(String.format("_:P%02d <http://acpj.googlecode.com/vocab#isClosed> \"%s\" .\n", portId, port.isClosed()).getBytes(ENCODING));
                portId++;
            }
            os.write(String.format("<%s> <http://acpj.googlecode.com/vocab#writePortArity> \"%s\" .\n", name, channel.getWritePortArity()).getBytes(ENCODING));
            for (Iterator<MonitoredPort> iterator2 = channel.getWritePorts(); iterator2.hasNext(); ) {
                MonitoredPort port = iterator2.next();
                os.write(String.format("<%s> <http://acpj.googlecode.com/vocab#hasPort> _:P%02d .\n", name, portId).getBytes(ENCODING));
                os.write(String.format("_:P%02d a <http://acpj.googlecode.com/vocab#WritePort> .\n", portId).getBytes(ENCODING));
                os.write(String.format("_:P%02d <http://acpj.googlecode.com/vocab#owningActor> <%s> .\n", portId, port.getOwningActor().getName()).getBytes(ENCODING));
                os.write(String.format("_:P%02d <http://acpj.googlecode.com/vocab#isClosed> \"%s\" .\n", portId, port.isClosed()).getBytes(ENCODING));
                portId++;
            }
        }
    }
