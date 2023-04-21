        protected void startNewCursor(final TriggerHistory triggerHistory, final TriggerRouter triggerRouter) {
            String initialLoadSql = symmetricDialect.createInitialLoadSqlFor(this.currentInitialLoadEvent.getNode(), triggerRouter, this.currentTable, triggerHistory, configurationService.getChannel(triggerRouter.getTrigger().getChannelId()));
            this.cursor = sqlTemplate.queryForCursor(initialLoadSql, new ISqlRowMapper<Data>() {

                public Data mapRow(Row rs) {
                    Data data = new Data(0, null, rs.stringValue(), DataEventType.INSERT, triggerHistory.getSourceTableName(), null, triggerHistory, batch.getChannelId(), null, null);
                    data.putAttribute(Data.ATTRIBUTE_ROUTER_ID, triggerRouter.getRouter().getRouterId());
                    return data;
                }
            });
        }
