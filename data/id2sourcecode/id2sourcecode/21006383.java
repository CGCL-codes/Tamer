    public LogView removeChannels(String regex, boolean remove) {
        for (Iterator it = iterator(); it.hasNext(); ) {
            Message next = (Message) it.next();
            if (next.getChannel().matches(regex) == remove) remove(next);
        }
        return this;
    }
