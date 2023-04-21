    public Conflict pickConflict(Table table, Batch batch) {
        Conflict settings = null;
        String fullyQualifiedName = table.getFullyQualifiedTableName();
        if (conflictSettingsByTable != null) {
            Conflict found = conflictSettingsByTable.get(fullyQualifiedName);
            if (found == null) {
                found = conflictSettingsByTable.get(table.getName());
            }
            if (found != null && (StringUtils.isBlank(found.getTargetChannelId()) || found.getTargetChannelId().equals(batch.getChannelId()))) {
                settings = found;
            }
        }
        if (settings == null && conflictSettingsByChannel != null) {
            settings = conflictSettingsByChannel.get(batch.getChannelId());
        }
        if (settings == null) {
            settings = defaultConflictSetting;
        }
        if (settings == null) {
            settings = new Conflict();
        }
        return settings;
    }
