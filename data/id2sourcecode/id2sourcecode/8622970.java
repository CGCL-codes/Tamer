    protected void updateOrCreateDatabaseTriggers(List<Trigger> triggers, StringBuilder sqlBuffer, boolean genAlways) {
        for (Trigger trigger : triggers) {
            TriggerHistory newestHistory = null;
            try {
                TriggerReBuildReason reason = TriggerReBuildReason.NEW_TRIGGERS;
                String errorMessage = null;
                Channel channel = configurationService.getChannel(trigger.getChannelId());
                if (channel == null) {
                    errorMessage = String.format("Trigger %s had an unrecognized channel_id of '%s'.  Please check to make sure the channel exists.  Creating trigger on the '%s' channel", trigger.getTriggerId(), trigger.getChannelId(), Constants.CHANNEL_DEFAULT);
                    log.error(errorMessage);
                    trigger.setChannelId(Constants.CHANNEL_DEFAULT);
                }
                Set<Table> tables = getTablesForTrigger(trigger, triggers);
                if (tables.size() > 0) {
                    for (Table table : tables) {
                        TriggerHistory latestHistoryBeforeRebuild = getNewestTriggerHistoryForTrigger(trigger.getTriggerId(), trigger.getSourceCatalogName(), trigger.getSourceSchemaName(), trigger.isSourceTableNameWildcarded() ? table.getName() : trigger.getSourceTableName());
                        boolean forceRebuildOfTriggers = false;
                        if (latestHistoryBeforeRebuild == null) {
                            reason = TriggerReBuildReason.NEW_TRIGGERS;
                            forceRebuildOfTriggers = true;
                        } else if (table.calculateTableHashcode() != latestHistoryBeforeRebuild.getTableHash()) {
                            reason = TriggerReBuildReason.TABLE_SCHEMA_CHANGED;
                            forceRebuildOfTriggers = true;
                        } else if (trigger.hasChangedSinceLastTriggerBuild(latestHistoryBeforeRebuild.getCreateTime()) || trigger.toHashedValue() != latestHistoryBeforeRebuild.getTriggerRowHash()) {
                            reason = TriggerReBuildReason.TABLE_SYNC_CONFIGURATION_CHANGED;
                            forceRebuildOfTriggers = true;
                        } else if (genAlways) {
                            reason = TriggerReBuildReason.FORCED;
                            forceRebuildOfTriggers = true;
                        }
                        boolean supportsTriggers = symmetricDialect.getPlatform().getDatabaseInfo().isTriggersSupported();
                        newestHistory = rebuildTriggerIfNecessary(sqlBuffer, forceRebuildOfTriggers, trigger, DataEventType.INSERT, reason, latestHistoryBeforeRebuild, null, trigger.isSyncOnInsert() && supportsTriggers, table);
                        newestHistory = rebuildTriggerIfNecessary(sqlBuffer, forceRebuildOfTriggers, trigger, DataEventType.UPDATE, reason, latestHistoryBeforeRebuild, newestHistory, trigger.isSyncOnUpdate() && supportsTriggers, table);
                        newestHistory = rebuildTriggerIfNecessary(sqlBuffer, forceRebuildOfTriggers, trigger, DataEventType.DELETE, reason, latestHistoryBeforeRebuild, newestHistory, trigger.isSyncOnDelete() && supportsTriggers, table);
                        if (latestHistoryBeforeRebuild != null && newestHistory != null) {
                            inactivateTriggerHistory(latestHistoryBeforeRebuild);
                        }
                        if (newestHistory != null) {
                            newestHistory.setErrorMessage(errorMessage);
                            if (parameterService.is(ParameterConstants.AUTO_SYNC_TRIGGERS)) {
                                if (this.triggerCreationListeners != null) {
                                    for (ITriggerCreationListener l : this.triggerCreationListeners) {
                                        l.triggerCreated(trigger, newestHistory);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    log.error("The configured table does not exist in the datasource that is configured: {}", trigger.qualifiedSourceTableName());
                    if (this.triggerCreationListeners != null) {
                        for (ITriggerCreationListener l : this.triggerCreationListeners) {
                            l.tableDoesNotExist(trigger);
                        }
                    }
                }
            } catch (Exception ex) {
                log.error(String.format("Failed to create triggers for %s", trigger.qualifiedSourceTableName()), ex);
                if (newestHistory != null) {
                    symmetricDialect.removeTrigger(null, trigger.getSourceCatalogName(), trigger.getSourceSchemaName(), newestHistory.getNameForInsertTrigger(), trigger.getSourceTableName(), newestHistory);
                    symmetricDialect.removeTrigger(null, trigger.getSourceCatalogName(), trigger.getSourceSchemaName(), newestHistory.getNameForUpdateTrigger(), trigger.getSourceTableName(), newestHistory);
                    symmetricDialect.removeTrigger(null, trigger.getSourceCatalogName(), trigger.getSourceSchemaName(), newestHistory.getNameForDeleteTrigger(), trigger.getSourceTableName(), newestHistory);
                }
                if (this.triggerCreationListeners != null) {
                    for (ITriggerCreationListener l : this.triggerCreationListeners) {
                        l.triggerFailed(trigger, ex);
                    }
                }
            }
        }
    }
