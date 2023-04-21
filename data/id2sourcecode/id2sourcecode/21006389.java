    public LogView removeMessages(String channels, String avatars, String regex, boolean remove) {
        for (Iterator it = iterator(); it.hasNext(); ) {
            Message next = (Message) it.next();
            if (next.getChannel().matches(regex)) {
                if (next.getAvatar().matches(regex)) {
                    if (next.getContent().matches(regex) == remove) remove(next);
                }
            }
        }
        return this;
    }
