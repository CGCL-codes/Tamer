    private List<TableRule> loadTableRule(Element current) throws InitialisationException {
        String name = current.getAttribute("name");
        String schemaName = current.getAttribute("schema");
        List<TableRule> list = new ArrayList<TableRule>();
        String[] names = new String[] { name };
        if (name != null) {
            names = name.split(",");
        }
        String defaultPools = current.getAttribute("defaultPools");
        String[] arrayDefaultPools = null;
        if (defaultPools != null) {
            arrayDefaultPools = readTokenizedString(defaultPools, " ,");
        }
        String readPools = current.getAttribute("readPools");
        String[] arrayReadPools = null;
        if (readPools != null) {
            arrayReadPools = readTokenizedString(readPools, " ,");
        }
        String writePools = current.getAttribute("writePools");
        String[] arrayWritePools = null;
        if (writePools != null) {
            arrayWritePools = readTokenizedString(writePools, " ,");
        }
        for (String tableName : names) {
            TableRule tableRule = new TableRule();
            Table table = new Table();
            String[] tableSchema = StringUtil.split(tableName, ".");
            if (tableSchema.length >= 2) {
                String tbName = tableName.substring(tableSchema[0].length() + 1);
                if ("*".equals(tbName)) {
                    tbName = "^*";
                }
                table.setName(tbName);
                Schema schema = new Schema();
                String sName = tableSchema[0];
                if ("*".equals(sName)) {
                    sName = "^*";
                }
                schema.setName(sName);
                table.setSchema(schema);
            } else {
                table.setName(tableName);
                if (!StringUtil.isEmpty(schemaName)) {
                    Schema schema = new Schema();
                    schema.setName(schemaName);
                    table.setSchema(schema);
                }
            }
            tableRule.defaultPools = arrayDefaultPools;
            tableRule.readPools = arrayReadPools;
            tableRule.writePools = arrayWritePools;
            tableRule.table = table;
            list.add(tableRule);
        }
        NodeList children = current.getChildNodes();
        int childSize = children.getLength();
        for (int i = 0; i < childSize; i++) {
            Node childNode = children.item(i);
            if (childNode instanceof Element) {
                Element child = (Element) childNode;
                final String nodeName = child.getNodeName();
                if (nodeName.equals("rule")) {
                    for (TableRule tableRule : list) {
                        tableRule.ruleList.add(loadRule(child, tableRule.table));
                    }
                }
            }
        }
        return list;
    }
