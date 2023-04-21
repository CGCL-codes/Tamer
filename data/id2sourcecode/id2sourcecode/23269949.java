    protected List<String> evaluate(StringBuffer loggerBuffer, T connection, V queryObject) {
        boolean isRead = true;
        boolean isPrepared = false;
        if (queryObject instanceof Request) {
            isRead = ((Request) queryObject).isRead();
            isPrepared = ((Request) queryObject).isPrepared();
        }
        List<String> poolNames = new ArrayList<String>();
        Map<Table, Map<Column, Comparative>> tables = evaluateTable(connection, queryObject);
        if (tables != null && tables.size() > 0) {
            Set<Map.Entry<Table, Map<Column, Comparative>>> entrySet = tables.entrySet();
            for (Map.Entry<Table, Map<Column, Comparative>> entry : entrySet) {
                boolean regexMatched = false;
                Map<Column, Comparative> columnMap = entry.getValue();
                TableRule tableRule = this.tableRuleMap.get(entry.getKey());
                Table table = entry.getKey();
                if (tableRule == null && table.getName() != null) {
                    for (Map.Entry<Table, TableRule> ruleEntry : this.regexTableRuleMap.entrySet()) {
                        Table ruleTable = ruleEntry.getKey();
                        boolean tableMatched = false;
                        boolean schemaMatched = false;
                        Pattern pattern = this.getPattern(ruleTable.getName());
                        java.util.regex.Matcher matcher = pattern.matcher(table.getName());
                        if (matcher.find()) {
                            tableMatched = true;
                        }
                        pattern = this.getPattern(ruleTable.getSchema().getName());
                        matcher = pattern.matcher(table.getSchema().getName());
                        if (matcher.find()) {
                            schemaMatched = true;
                        }
                        if (tableMatched && schemaMatched) {
                            tableRule = ruleEntry.getValue();
                            regexMatched = true;
                            break;
                        }
                    }
                }
                if (tableRule != null) {
                    if (columnMap == null || isPrepared) {
                        String[] pools = (isRead ? tableRule.readPools : tableRule.writePools);
                        if (pools == null || pools.length == 0) {
                            pools = tableRule.defaultPools;
                        }
                        for (String poolName : pools) {
                            if (!poolNames.contains(poolName)) {
                                poolNames.add(poolName);
                            }
                        }
                        if (!isPrepared) {
                            if (logger.isDebugEnabled()) {
                                loggerBuffer.append(", no Column rule, using table:" + tableRule.table + " default rules:" + Arrays.toString(tableRule.defaultPools));
                            }
                        }
                        continue;
                    }
                    List<String> groupMatched = new ArrayList<String>();
                    for (Rule rule : tableRule.ruleList) {
                        if (rule.group != null) {
                            if (groupMatched.contains(rule.group)) {
                                continue;
                            }
                        }
                        if (columnMap.size() < rule.parameterMap.size()) {
                            continue;
                        } else {
                            boolean matched = true;
                            for (Column exclude : rule.excludes) {
                                Comparable<?> condition = columnMap.get(exclude);
                                if (condition != null) {
                                    matched = false;
                                    break;
                                }
                            }
                            if (!matched) {
                                continue;
                            }
                            Comparable<?>[] comparables = new Comparable[rule.parameterMap.size()];
                            for (Map.Entry<Column, Integer> parameter : rule.cloumnMap.entrySet()) {
                                Comparative condition = null;
                                if (regexMatched) {
                                    Column column = new Column();
                                    column.setName(parameter.getKey().getName());
                                    column.setTable(table);
                                    condition = columnMap.get(column);
                                } else {
                                    condition = columnMap.get(parameter.getKey());
                                }
                                if (condition != null) {
                                    if (rule.ignoreArray && condition instanceof ComparativeBaseList) {
                                        matched = false;
                                        break;
                                    }
                                    comparables[parameter.getValue()] = (Comparative) condition.clone();
                                } else {
                                    matched = false;
                                    break;
                                }
                            }
                            if (!matched) {
                                continue;
                            }
                            try {
                                Comparable<?> result = rule.rowJep.getValue(comparables);
                                Integer i = 0;
                                if (result instanceof Comparative) {
                                    if (rule.result == RuleResult.INDEX) {
                                        i = (Integer) ((Comparative) result).getValue();
                                        if (i < 0) {
                                            continue;
                                        }
                                        matched = true;
                                    } else if (rule.result == RuleResult.POOLNAME) {
                                        String matchedPoolsString = ((Comparative) result).getValue().toString();
                                        String[] poolNamesMatched = matchedPoolsString.split(",");
                                        if (poolNamesMatched != null && poolNamesMatched.length > 0) {
                                            for (String poolName : poolNamesMatched) {
                                                if (!poolNames.contains(poolName)) {
                                                    poolNames.add(poolName);
                                                }
                                            }
                                            if (logger.isDebugEnabled()) {
                                                loggerBuffer.append(", matched table:" + tableRule.table + ", rule:" + rule.name);
                                            }
                                        }
                                        continue;
                                    } else {
                                        matched = (Boolean) ((Comparative) result).getValue();
                                    }
                                } else {
                                    if (rule.result == RuleResult.INDEX) {
                                        i = (Integer) Integer.valueOf(result.toString());
                                        if (i < 0) {
                                            continue;
                                        }
                                        matched = true;
                                    } else if (rule.result == RuleResult.POOLNAME) {
                                        String matchedPoolsString = result.toString();
                                        String[] poolNamesMatched = StringUtil.split(matchedPoolsString, ";,");
                                        if (poolNamesMatched != null && poolNamesMatched.length > 0) {
                                            for (String poolName : poolNamesMatched) {
                                                if (!poolNames.contains(poolName)) {
                                                    poolNames.add(poolName);
                                                }
                                            }
                                            if (logger.isDebugEnabled()) {
                                                loggerBuffer.append(", matched table:" + tableRule.table + ", rule:" + rule.name);
                                            }
                                        }
                                        continue;
                                    } else {
                                        matched = (Boolean) result;
                                    }
                                }
                                if (matched) {
                                    if (rule.group != null) {
                                        groupMatched.add(rule.group);
                                    }
                                    String[] pools = (isRead ? rule.readPools : rule.writePools);
                                    if (pools == null || pools.length == 0) {
                                        pools = rule.defaultPools;
                                    }
                                    if (pools != null && pools.length > 0) {
                                        if (rule.isSwitch) {
                                            if (!poolNames.contains(pools[i])) {
                                                poolNames.add(pools[i]);
                                            }
                                        } else {
                                            for (String poolName : pools) {
                                                if (!poolNames.contains(poolName)) {
                                                    poolNames.add(poolName);
                                                }
                                            }
                                        }
                                    } else {
                                        logger.error("rule:" + rule.name + " matched, but pools is null");
                                    }
                                    if (logger.isDebugEnabled()) {
                                        loggerBuffer.append(", matched table:" + tableRule.table + ", rule:" + rule.name);
                                    }
                                }
                            } catch (com.meidusa.amoeba.sqljep.ParseException e) {
                            }
                        }
                    }
                    if (poolNames.size() == 0) {
                        String[] pools = (isRead ? tableRule.readPools : tableRule.writePools);
                        if (pools == null || pools.length == 0) {
                            pools = tableRule.defaultPools;
                        }
                        if (!isPrepared) {
                            if (tableRule.ruleList != null && tableRule.ruleList.size() > 0) {
                                if (logger.isDebugEnabled()) {
                                    loggerBuffer.append(", no rule matched, using tableRule:[" + tableRule.table + "] defaultPools");
                                }
                            } else {
                                if (logger.isDebugEnabled()) {
                                    if (pools != null) {
                                        StringBuffer buffer = new StringBuffer();
                                        for (String pool : pools) {
                                            buffer.append(pool).append(",");
                                        }
                                        loggerBuffer.append(", using tableRule:[" + tableRule.table + "] defaultPools=" + buffer.toString());
                                    }
                                }
                            }
                        }
                        for (String poolName : pools) {
                            if (!poolNames.contains(poolName)) {
                                poolNames.add(poolName);
                            }
                        }
                    }
                }
            }
        }
        return poolNames;
    }
