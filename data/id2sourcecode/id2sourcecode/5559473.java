    @Override
    protected Criteria createCriteria(SearchFilter<GameAccount> filter) {
        Criteria criteria = getSession().createCriteria(GameAccount.class);
        if (filter instanceof GameAccountFilter) {
            GameAccountFilter cf = (GameAccountFilter) filter;
            if (cf.getAccountName() != null) {
                criteria.add(Restrictions.eq("name", cf.getAccountName()));
            }
            if (cf.getChannelWebServiceUID() != null || cf.getBotLinked() != null) {
                criteria.createAlias("userGameRealm", "userGameRealm");
                criteria.createAlias("userGameRealm.realm", "realm");
                if (cf.getChannelWebServiceUID() != null) {
                    criteria.createAlias("realm.channels", "channel");
                    criteria.add(Restrictions.eq("channel.webServiceUserId", cf.getChannelWebServiceUID()));
                }
            }
        }
        return criteria;
    }
