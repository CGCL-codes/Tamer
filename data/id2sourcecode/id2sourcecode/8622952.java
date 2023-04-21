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
