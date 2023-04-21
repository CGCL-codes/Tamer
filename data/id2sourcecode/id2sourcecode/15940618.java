    private void init() {
        log = new DebugLogger("Accounting");
        accountsWithPasswords = null;
        accountsWithPasswordsCoding = null;
        accountsWithPasswordsSeed = null;
        MD5Digester = null;
        Global.accounting = this;
        if (Global.serviceStatus != null) {
            ChannelUseDescriptionCollection cudc = Global.serviceStatus.getServiceDescription().getChannelUses();
            cudc.add(new ChannelUseDescription(Global.params().getParamString(ParamServer.ACCOUNTING_CHANNEL), "reception of accounting passwords and keys", 0L, 0L, null, new EventDescriptionCollection(new EventDescription(ACCOUNT_NAMESPACE, "account information"))));
        }
    }
