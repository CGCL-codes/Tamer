    private Rule loadRule(Element current, Table table) throws InitialisationException {
        Rule rule = new Rule();
        rule.name = current.getAttribute("name");
        String group = current.getAttribute("group");
        rule.group = StringUtil.isEmpty(group) ? null : group;
        String ignoreArray = current.getAttribute("ignoreArray");
        rule.ignoreArray = Boolean.parseBoolean(ignoreArray);
        String isSwitch = current.getAttribute("isSwitch");
        rule.isSwitch = Boolean.parseBoolean(isSwitch);
        String result = current.getAttribute("ruleResult");
        if (!StringUtil.isEmpty(result)) {
            result = result.toUpperCase();
            rule.result = Enum.valueOf(RuleResult.class, result);
        }
        Element parametersNode = DocumentUtil.getTheOnlyElement(current, "parameters");
        if (parametersNode != null) {
            String[] tokens = readTokenizedString(parametersNode.getTextContent(), " ,");
            int index = 0;
            for (String parameter : tokens) {
                rule.parameterMap.put(parameter, index);
                Column column = new Column();
                column.setName(parameter);
                column.setTable(table);
                rule.cloumnMap.put(column, index);
                index++;
            }
            tokens = readTokenizedString(parametersNode.getAttribute("excludes"), " ,");
            if (tokens != null) {
                for (String parameter : tokens) {
                    Column column = new Column();
                    column.setName(parameter);
                    column.setTable(table);
                    rule.excludes.add(column);
                }
            }
        }
        Element expression = DocumentUtil.getTheOnlyElement(current, "expression");
        rule.expression = expression.getTextContent();
        rule.rowJep = new RowJEP(rule.expression);
        try {
            rule.rowJep.parseExpression(rule.parameterMap, variableMap, this.ruleFunctionMap);
        } catch (com.meidusa.amoeba.sqljep.ParseException e) {
            throw new InitialisationException("parser expression:" + rule.expression + " error", e);
        }
        Element defaultPoolsNode = DocumentUtil.getTheOnlyElement(current, "defaultPools");
        if (defaultPoolsNode != null) {
            String defaultPools = defaultPoolsNode.getTextContent();
            rule.defaultPools = readTokenizedString(defaultPools, " ,");
        }
        Element readPoolsNode = DocumentUtil.getTheOnlyElement(current, "readPools");
        if (readPoolsNode != null) {
            rule.readPools = readTokenizedString(readPoolsNode.getTextContent(), " ,");
        }
        Element writePoolsNode = DocumentUtil.getTheOnlyElement(current, "writePools");
        if (writePoolsNode != null) {
            rule.writePools = readTokenizedString(writePoolsNode.getTextContent(), " ,");
        }
        return rule;
    }
