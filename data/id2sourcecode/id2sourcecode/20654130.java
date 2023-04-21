    protected DatabaseWriterSettings buildDatabaseWriterSettings(List<IDatabaseWriterFilter> filters, List<? extends Conflict> conflictSettings, List<ResolvedData> resolvedDatas) {
        DatabaseWriterSettings settings = new DatabaseWriterSettings();
        settings.setDatabaseWriterFilters(filters);
        settings.setMaxRowsBeforeCommit(parameterService.getLong(ParameterConstants.DATA_LOADER_MAX_ROWS_BEFORE_COMMIT));
        settings.setTreatDateTimeFieldsAsVarchar(parameterService.is(ParameterConstants.DATA_LOADER_TREAT_DATETIME_AS_VARCHAR));
        Map<String, Conflict> byChannel = new HashMap<String, Conflict>();
        Map<String, Conflict> byTable = new HashMap<String, Conflict>();
        if (conflictSettings != null) {
            for (Conflict conflictSetting : conflictSettings) {
                String qualifiedTableName = conflictSetting.toQualifiedTableName();
                if (StringUtils.isNotBlank(qualifiedTableName)) {
                    byTable.put(qualifiedTableName, conflictSetting);
                } else if (StringUtils.isNotBlank(conflictSetting.getTargetChannelId())) {
                    byChannel.put(conflictSetting.getTargetChannelId(), conflictSetting);
                } else {
                    settings.setDefaultConflictSetting(conflictSetting);
                }
            }
        }
        settings.setConflictSettingsByChannel(byChannel);
        settings.setConflictSettingsByTable(byTable);
        settings.setResolvedData(resolvedDatas);
        return settings;
    }
