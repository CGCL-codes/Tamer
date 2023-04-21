    public void save(ConflictSettingNodeGroupLink setting) {
        this.lastConflictCacheResetTimeInMs = 0;
        if (sqlTemplate.update(getSql("updateConflictSettingsSql"), setting.getNodeGroupLink().getSourceNodeGroupId(), setting.getNodeGroupLink().getTargetNodeGroupId(), setting.getTargetChannelId(), setting.getTargetCatalogName(), setting.getTargetSchemaName(), setting.getTargetTableName(), setting.getDetectType().name(), setting.getResolveType().name(), setting.isResolveChangesOnly() ? 1 : 0, setting.isResolveRowOnly() ? 1 : 0, setting.getDetectExpression(), setting.getLastUpdateBy(), setting.getConflictId()) == 0) {
            sqlTemplate.update(getSql("insertConflictSettingsSql"), setting.getNodeGroupLink().getSourceNodeGroupId(), setting.getNodeGroupLink().getTargetNodeGroupId(), setting.getTargetChannelId(), setting.getTargetCatalogName(), setting.getTargetSchemaName(), setting.getTargetTableName(), setting.getDetectType().name(), setting.getResolveType().name(), setting.isResolveChangesOnly() ? 1 : 0, setting.isResolveRowOnly() ? 1 : 0, setting.getDetectExpression(), setting.getLastUpdateBy(), setting.getConflictId());
        }
    }
