    protected TriggerHistory rebuildTriggerIfNecessary(StringBuilder sqlBuffer, boolean forceRebuild, Trigger trigger, DataEventType dmlType, TriggerReBuildReason reason, TriggerHistory oldhist, TriggerHistory hist, boolean triggerIsActive, Table table) {
        boolean triggerExists = false;
        boolean triggerRemoved = false;
        TriggerHistory newTriggerHist = new TriggerHistory(table, trigger, reason);
        int maxTriggerNameLength = symmetricDialect.getMaxTriggerNameLength();
        newTriggerHist.setNameForInsertTrigger(getTriggerName(DataEventType.INSERT, maxTriggerNameLength, trigger, table).toUpperCase());
        newTriggerHist.setNameForUpdateTrigger(getTriggerName(DataEventType.UPDATE, maxTriggerNameLength, trigger, table).toUpperCase());
        newTriggerHist.setNameForDeleteTrigger(getTriggerName(DataEventType.DELETE, maxTriggerNameLength, trigger, table).toUpperCase());
        String oldTriggerName = null;
        String oldSourceSchema = null;
        String oldCatalogName = null;
        if (oldhist != null) {
            oldTriggerName = oldhist.getTriggerNameForDmlType(dmlType);
            oldSourceSchema = oldhist.getSourceSchemaName();
            oldCatalogName = oldhist.getSourceCatalogName();
            triggerExists = symmetricDialect.doesTriggerExist(oldCatalogName, oldSourceSchema, oldhist.getSourceTableName(), oldTriggerName);
        } else {
            oldTriggerName = newTriggerHist.getTriggerNameForDmlType(dmlType);
            oldSourceSchema = trigger.getSourceSchemaName();
            oldCatalogName = trigger.getSourceCatalogName();
            triggerExists = symmetricDialect.doesTriggerExist(oldCatalogName, oldSourceSchema, trigger.getSourceTableName(), oldTriggerName);
        }
        if (!triggerExists && forceRebuild) {
            reason = TriggerReBuildReason.TRIGGERS_MISSING;
        }
        if ((forceRebuild || !triggerIsActive) && triggerExists) {
            symmetricDialect.removeTrigger(sqlBuffer, oldCatalogName, oldSourceSchema, oldTriggerName, trigger.getSourceTableName(), oldhist);
            triggerExists = false;
            triggerRemoved = true;
        }
        boolean isDeadTrigger = !trigger.isSyncOnInsert() && !trigger.isSyncOnUpdate() && !trigger.isSyncOnDelete();
        if (hist == null && (oldhist == null || (!triggerExists && triggerIsActive) || (isDeadTrigger && forceRebuild))) {
            insert(newTriggerHist);
            hist = getNewestTriggerHistoryForTrigger(trigger.getTriggerId(), trigger.getSourceCatalogName(), trigger.getSourceSchemaName(), trigger.isSourceTableNameWildcarded() ? table.getName() : trigger.getSourceTableName());
        }
        try {
            if (!triggerExists && triggerIsActive) {
                symmetricDialect.createTrigger(sqlBuffer, dmlType, trigger, hist, configurationService.getChannel(trigger.getChannelId()), tablePrefix, table);
                if (triggerRemoved) {
                    statisticManager.incrementTriggersRebuiltCount(1);
                } else {
                    statisticManager.incrementTriggersCreatedCount(1);
                }
            } else if (triggerRemoved) {
                statisticManager.incrementTriggersRemovedCount(1);
            }
        } catch (RuntimeException ex) {
            if (!symmetricDialect.doesTriggerExist(hist.getSourceCatalogName(), hist.getSourceSchemaName(), hist.getSourceTableName(), hist.getTriggerNameForDmlType(dmlType))) {
                log.warn("Cleaning up trigger hist row of {} after failing to create the associated trigger", hist.getTriggerHistoryId());
                hist.setErrorMessage(ex.getMessage());
                inactivateTriggerHistory(hist);
            }
            throw ex;
        }
        return hist;
    }
