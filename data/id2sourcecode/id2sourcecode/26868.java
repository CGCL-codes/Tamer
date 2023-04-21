    public boolean hasTicketPending(String user, String channel) {
        if (tickets.containsKey(user.toLowerCase() + " " + channel.toLowerCase())) {
            UserTicket t = tickets.get(user.toLowerCase() + " " + channel.toLowerCase());
            if (t.getChannel().equals(channel.toLowerCase()) && t.getTime() > Long.parseLong(C.get_time())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
