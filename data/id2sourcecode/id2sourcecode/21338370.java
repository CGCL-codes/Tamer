    @Override
    protected Criteria createCriteria(SearchFilter<BattleNetUserPresence> filter) {
        Criteria criteria = getSession().createCriteria(BattleNetUserPresence.class);
        if (filter instanceof BattleNetUserPresenceFilter) {
            BattleNetUserPresenceFilter cf = (BattleNetUserPresenceFilter) filter;
            criteria.add(Restrictions.eq("channel", cf.getChannel()));
            if (cf.getAccountName() != null) {
                criteria.add(Restrictions.eq("username", cf.getAccountName()));
            }
        }
        return criteria;
    }
