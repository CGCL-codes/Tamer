    protected void setTable(String tableName) {
        cleanupAfterDataLoad();
        context.setTableName(tableName);
        if (context.getTableTemplate() == null) {
            String schema = null;
            String catalog = null;
            if (parameterService.is(ParameterConstants.DATA_LOADER_LOOKUP_TARGET_SCHEMA)) {
                Node sourceNode = nodeService.findNode(context.getNodeId());
                Node targetNode = nodeService.findIdentity();
                if (sourceNode != null) {
                    Trigger trigger = null;
                    if (targetNode == null) {
                        trigger = configurationService.getTriggerFor(tableName, sourceNode.getNodeGroupId());
                    } else {
                        trigger = configurationService.getTriggerForTarget(tableName, sourceNode.getNodeGroupId(), targetNode.getNodeGroupId(), context.getChannelId());
                        if (trigger != null && !StringUtils.isBlank(trigger.getTargetTableName())) {
                            tableName = trigger.getTargetTableName();
                        }
                        if (trigger != null && !StringUtils.isBlank(trigger.getTargetSchemaName())) {
                            schema = trigger.getTargetSchemaName();
                        }
                        if (trigger != null && !StringUtils.isBlank(trigger.getTargetCatalogName())) {
                            catalog = trigger.getTargetCatalogName();
                        }
                    }
                }
            }
            context.setTableTemplate(new TableTemplate(jdbcTemplate, dbDialect, tableName, this.columnFilters != null ? this.columnFilters.get(tableName) : null, parameterService.is(ParameterConstants.DATA_LOADER_NO_KEYS_IN_UPDATE), schema, catalog));
        }
        prepareTableForDataLoad();
    }
