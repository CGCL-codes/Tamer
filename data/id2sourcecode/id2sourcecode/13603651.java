    public void populateServerFields() {
        makeTransactions();
        makeTranslations();
        makeArchivePolicies();
        makeAlarmChecks();
        for (int i = 0; i < itsInputTransactions.length; i++) {
            Transaction thistrans = itsInputTransactions[i];
            if (thistrans != null && thistrans.getChannel() != null && !thistrans.getChannel().equals("NONE")) {
                ExternalSystem ds = ExternalSystem.getExternalSystem(thistrans.getChannel());
                if (ds != null) {
                    ds.addPoint(this);
                } else {
                    theirLogger.warn("(" + getFullName() + ") No ExternalSystem found for Channel: " + thistrans.getChannel());
                }
            }
        }
        setArchiver(PointArchiver.getPointArchiver());
    }
