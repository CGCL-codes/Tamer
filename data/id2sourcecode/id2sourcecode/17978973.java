    private void generateRootClassMapping(StringBuffer buffer, OpPrototype rootType) {
        if (rootType.isInterface()) {
            return;
        }
        int level = 1;
        buffer.append(generateIndent(level)).append("<class name=\"").append(rootType.getInstanceClass().getName());
        buffer.append("\" polymorphism=\"implicit\" ");
        if (USE_DYNAMIC) {
            buffer.append("dynamic-insert=\"true\" ");
            buffer.append("dynamic-update=\"true\" ");
        }
        String tableName = generateTableName(rootType.getName());
        rootType.setTableName(tableName);
        buffer.append("table=\"").append(tableName).append("\">").append(NEW_LINE);
        appendLine(buffer, "<cache usage=\"read-write\"/>", level + 1);
        appendLine(buffer, "<id name=\"id\" column=\"op_id\" type=\"long\" access=\"field\">", level + 1);
        appendLine(buffer, "<generator class=\"" + ID_GENERATOR_CLASS + "\">", level + 2);
        appendLine(buffer, "<param name=\"table\">" + OpHibernateSource.HILO_GENERATOR_TABLE_NAME + "</param>", level + 3);
        appendLine(buffer, "<param name=\"column\">" + OpHibernateSource.HILO_GENERATOR_COLUMN_NAME + "</param>", level + 3);
        appendLine(buffer, "</generator>", level + 2);
        appendLine(buffer, "</id>", level + 1);
        addMembers(buffer, rootType, level + 1);
        Iterator subTypesIt = rootType.subTypes();
        while (subTypesIt.hasNext()) {
            OpPrototype subType = (OpPrototype) subTypesIt.next();
            appendMapping(buffer, subType, level + 1);
        }
        addFiltersUsage(rootType.getInstanceClass(), buffer, level + 1);
        buffer.append(generateIndent(level)).append("</class>").append(NEW_LINE);
    }
