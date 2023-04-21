    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Note)) return false; else {
            Note n = (Note) obj;
            return (getOnset() == n.getOnset() && getPitch().equals(n.getPitch()) && getChannel() == n.getChannel() && getStaff() == n.getStaff() && getDuration() == n.getDuration()) && getVoice() == n.getVoice();
        }
    }
