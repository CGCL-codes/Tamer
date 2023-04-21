    public String translate(SpectatorListMessage m, Locale locale) {
        StringBuilder message = new StringBuilder();
        message.append("speclist #");
        message.append(m.getChannel());
        for (String spectator : m.getSpectators()) {
            message.append(" ");
            message.append(spectator);
        }
        return message.toString();
    }
