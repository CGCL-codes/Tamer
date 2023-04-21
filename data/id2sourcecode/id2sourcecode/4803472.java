    String getThreadName() {
        String name = processor.getName();
        name += "[";
        for (Iterator<InputPort<?>> iter = ports.getInputPorts().iterator(); iter.hasNext(); ) {
            name += iter.next().getChannel().getId();
            if (iter.hasNext()) name += ",";
        }
        name += " --> ";
        for (Iterator<OutputPort<?>> iter = ports.getOutputPorts().iterator(); iter.hasNext(); ) {
            name += iter.next().getChannel().getId();
            if (iter.hasNext()) name += ",";
        }
        name += "]";
        ;
        return name + "-" + (++thread_count);
    }
