                public Data mapRow(Row rs) {
                    Data data = new Data(0, null, rs.stringValue(), DataEventType.INSERT, triggerHistory.getSourceTableName(), null, triggerHistory, batch.getChannelId(), null, null);
                    data.putAttribute(Data.ATTRIBUTE_ROUTER_ID, triggerRouter.getRouter().getRouterId());
                    return data;
                }
