    protected EpgEvent checkEvent(EpgEvent evt) {
        EpgEvent rv = evt;
        List<ConditionElement> args = new ArrayList<ConditionElement>();
        args.add(new EqualConditionElement("channel", evt.getChannel()));
        args.add(new EqualConditionElement("epgId", evt.getEpgId()));
        Transaction ta = getTransactionFactory().createTransaction();
        TORead<EpgEvent> tor = new TORead<EpgEvent>(EpgEvent.class, args);
        ta.add(tor);
        ta.setRollbackOnly();
        ta.execute();
        List<EpgEvent> tmp = tor.getResult();
        if (tmp != null && tmp.size() > 0) {
            rv = tmp.get(0);
            rv.setChannel(evt.getChannel());
        }
        return rv;
    }
