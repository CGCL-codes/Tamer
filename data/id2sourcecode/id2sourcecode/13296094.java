    public void addAudit(MPSChannelAudit audit) {
        audits.add(audit);
        if (audit.getChannel() != this) audit.setChannel(this);
    }
