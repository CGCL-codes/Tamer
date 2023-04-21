    public void execute(IFile erdFile, RootModel root, GraphicalViewer viewer) {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setFilterExtensions(new String[] { "*.xls" });
        String path = dialog.open();
        if (path == null) {
            return;
        }
        List<TableData> tables = new ArrayList<TableData>();
        for (TableModel table : root.getTables()) {
            TableData tableData = new TableData();
            tableData.setLogicalTableName(table.getLogicalName());
            tableData.setPhysicalTableName(table.getTableName());
            tableData.setDescription(table.getDescription());
            List<ColumnData> columns = new ArrayList<ColumnData>();
            for (ColumnModel column : table.getColumns()) {
                ColumnData columnData = new ColumnData();
                columnData.setLogicalColumnName(column.getLogicalName());
                columnData.setPhysicalColumnName(column.getColumnName());
                columnData.setDescription(column.getDescription());
                columnData.setType(column.getColumnType().getName());
                columnData.setDefaultValue(column.getDefaultValue());
                if (column.getColumnType().supportSize()) {
                    columnData.setSize(column.getSize());
                }
                if (column.isPrimaryKey()) {
                    columnData.setPrimaryKey(DBPlugin.getResourceString("label.o"));
                }
                if (column.isNotNull()) {
                    columnData.setNullable(DBPlugin.getResourceString("label.x"));
                }
                LOOP: for (AbstractDBConnectionModel conn : table.getModelSourceConnections()) {
                    if (conn instanceof ForeignKeyModel) {
                        ForeignKeyModel foreignKey = (ForeignKeyModel) conn;
                        ForeignKeyMapping[] mappings = foreignKey.getMapping();
                        for (ForeignKeyMapping mapping : mappings) {
                            if (mapping.getRefer() == column) {
                                columnData.setForeignKey(DBPlugin.getResourceString("label.o"));
                                columnData.setReference(((TableModel) foreignKey.getTarget()).getTableName() + "." + mapping.getTarget().getColumnName());
                                break LOOP;
                            }
                        }
                    }
                }
                columnData.setIndex(columns.size() + 1);
                columns.add(columnData);
            }
            tableData.setColumns(columns);
            tables.add(tableData);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("tables", tables);
        InputStream in = getClass().getResourceAsStream("template.xls");
        FPTemplate template = new FPTemplate();
        try {
            HSSFWorkbook wb = template.process(in, data);
            FileOutputStream fos = new FileOutputStream(path);
            wb.write(fos);
            IOUtils.close(fos);
        } catch (Exception ex) {
            DBPlugin.logException(ex);
        }
    }
