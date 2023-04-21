    @Override
    public BattleNetChannel getChannelByUID(String identifier) {
        Criteria criteria = getSession().createCriteria(BattleNetChannel.class);
        criteria.add(Restrictions.eq("webServiceUserId", identifier));
        return (BattleNetChannel) criteria.uniqueResult();
    }
