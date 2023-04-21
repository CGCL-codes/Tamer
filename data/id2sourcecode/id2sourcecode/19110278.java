    public int compareTo(Note n) {
        if (equals(n)) return 0;
        if (getOnset() - n.getOnset() != 0l) return (getOnset() - n.getOnset()) > 0l ? 1 : -1;
        if (getPitch().compareTo(n.getPitch()) != 0) return getPitch().compareTo(n.getPitch());
        if ((getDuration() != null) && (n.getDuration() != null) && (getDuration() - n.getDuration() != 0l)) return (getDuration() - n.getDuration()) > 0l ? 1 : -1;
        if ((getChannel() != null) && (n.getChannel() != null) && (getChannel() - n.getChannel() != 0)) return getChannel() - n.getChannel();
        if (getVoice() - n.getVoice() != 0) return (getVoice() - n.getVoice()) > 0 ? 1 : -1;
        if ((getStaff() != null) && (n.getStaff() != null) && (getStaff() - n.getStaff() != 0)) return getStaff() - n.getStaff();
        return 0;
    }
