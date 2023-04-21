    void doRetransmit(String identityRegex, IType type, IDomain domain) {
        getChannel().retransmit(identityRegex, type, domain);
    }
