    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TVProgramme)) {
            return false;
        }
        TVProgramme other = (TVProgramme) obj;
        if (title.equals(other.getTitle()) && (start == other.getStart()) && channel.equals(other.getChannel())) {
            return true;
        }
        return false;
    }
