    public int compareTo(Object o) throws ClassCastException {
        Message anotherMessage = (Message) o;
        int dateCompare = getDate().compareTo(anotherMessage.getDate());
        if (dateCompare == 0) {
            int chanCompare = getChannel().compareTo(anotherMessage.getChannel());
            if (chanCompare == 0) {
                int avCompare = getAvatar().compareTo(anotherMessage.getAvatar());
                if (avCompare == 0) return getContent().compareTo(anotherMessage.getContent()); else return avCompare;
            } else return chanCompare;
        } else return dateCompare;
    }
