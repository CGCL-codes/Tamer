    public CommonSetPatch(final PatchParameters parameters) {
        super(ID, "*81#".length() + (parameters.size() * 8));
        this.parameters = parameters;
        Util.validate("parameters.size()", parameters.size(), 1, 255);
        int offset = 3;
        for (int i = 0; i < parameters.size(); i++) {
            set4(offset, parameters.getDimmerId(i) + 1);
            offset += 4;
            set4(offset, parameters.getChannelId(i) + 1);
            offset += 4;
        }
    }
