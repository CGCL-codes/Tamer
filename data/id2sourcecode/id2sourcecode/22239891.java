    public void removeMember(Account member) {
        members.remove(member);
        member.getChannels().remove(this);
    }
