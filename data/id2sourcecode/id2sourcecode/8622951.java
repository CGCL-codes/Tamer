    public Map<String, List<TriggerRouter>> getTriggerRoutersByChannel(String nodeGroupId) {
        final Map<String, List<TriggerRouter>> retMap = new HashMap<String, List<TriggerRouter>>();
        sqlTemplate.query(getTriggerRouterSql("selectGroupTriggersSql"), new TriggerRouterMapper() {

            @Override
            public TriggerRouter mapRow(Row rs) {
                TriggerRouter tr = super.mapRow(rs);
                List<TriggerRouter> list = retMap.get(tr.getTrigger().getChannelId());
                if (list == null) {
                    list = new ArrayList<TriggerRouter>();
                    retMap.put(tr.getTrigger().getChannelId(), list);
                }
                list.add(tr);
                return tr;
            }

            ;
        }, nodeGroupId);
        return retMap;
    }
