    public void findReferences(TableMetaData tableMetaData, List<ForeignKey> importedFKsAnalized, List<ForeignKey> exportedFKsAnalized, String referencedBy, String referenceHist, boolean imported, boolean exported) throws Exception {
        IOHelper.writeInfo("Finding references of " + tableMetaData.getSchema() + "." + tableMetaData.getTableName());
        if (importedFKsAnalized == null) importedFKsAnalized = new ArrayList<ForeignKey>(0);
        if (exportedFKsAnalized == null) exportedFKsAnalized = new ArrayList<ForeignKey>(0);
        List<TableMetaData> references = new ArrayList<TableMetaData>(0);
        List<TableMetaData> importedReferences = new ArrayList<TableMetaData>(0);
        List<TableMetaData> exportedReferences = new ArrayList<TableMetaData>(0);
        if (referenceHist == null) referenceHist = tableMetaData.getTableName().toUpperCase(); else referenceHist += "->" + tableMetaData.getTableName().toUpperCase();
        for (ForeignKey fk : tableMetaData.getImportedKeys()) {
            if (!importedFKsAnalized.contains(fk) && !fk.getReferenceTable().equalsIgnoreCase(referencedBy) && !tableMetaData.getTableName().equalsIgnoreCase(fk.getReferenceTable())) {
                IOHelper.writeInfo("Analizing imported fk " + fk.getConstraintName() + " from " + referenceHist);
                importedFKsAnalized.add(fk);
                TableMetaData fkMetaData = analizeTable(tableMetaData.getSchema(), fk.getReferenceTable());
                fkMetaData.setType(TableMetaData.MD_FROM_IMPORTED_KEY);
                references.add(fkMetaData);
                importedReferences.add(fkMetaData);
            } else {
                IOHelper.writeInfo(fk.getConstraintName() + " already analized");
            }
        }
        for (ForeignKey fk : tableMetaData.getExportedKeys()) {
            if (!importedFKsAnalized.contains(fk) && !fk.getReferenceTable().equalsIgnoreCase(referencedBy) && !tableMetaData.getTableName().equalsIgnoreCase(fk.getReferenceTable()) && exported) {
                IOHelper.writeInfo("Analizing exported fk " + fk.getConstraintName() + " from " + referenceHist);
                exportedFKsAnalized.add(fk);
                TableMetaData fkMetaData = analizeTable(tableMetaData.getSchema(), fk.getReferenceTable());
                fkMetaData.setType(TableMetaData.MD_FROM_EXPORTED_KEY);
                references.add(fkMetaData);
                exportedReferences.add(fkMetaData);
            } else {
                IOHelper.writeInfo(fk.getConstraintName() + " already analized");
            }
        }
        for (TableMetaData tmd : references) {
            List<TableMetaData> ref2Pass = new ArrayList<TableMetaData>();
            for (ForeignKey fk : tmd.getImportedKeys()) {
                if (!importedFKsAnalized.contains(fk) && !fk.getReferenceTable().equalsIgnoreCase(referencedBy) && !tmd.getTableName().equalsIgnoreCase(fk.getReferenceTable())) {
                    IOHelper.writeInfo("Analizing 2 pass imported fk " + fk.getConstraintName() + " from " + referenceHist + "->" + tmd.getTableName());
                    importedFKsAnalized.add(fk);
                    TableMetaData fkMetaData = analizeTable(tmd.getSchema(), fk.getReferenceTable());
                    fkMetaData.setType(TableMetaData.MD_FROM_IMPORTED_KEY);
                    findReferences(fkMetaData, importedFKsAnalized, exportedFKsAnalized, tmd.getTableName(), referenceHist + "->" + tmd.getTableName(), true, false);
                    ref2Pass.add(fkMetaData);
                } else {
                    IOHelper.writeInfo(fk.getConstraintName() + " already analized");
                }
            }
            tmd.getReferences().addAll(ref2Pass);
            tmd.getReferencesImported().addAll(ref2Pass);
        }
        for (TableMetaData tmd : references) {
            List<TableMetaData> ref2Pass = new ArrayList<TableMetaData>();
            List<TableMetaData> exportRef2Pass = new ArrayList<TableMetaData>();
            for (ForeignKey fk : tmd.getExportedKeys()) {
                if (!exportedFKsAnalized.contains(fk) && !fk.getReferenceTable().equalsIgnoreCase(referencedBy) && !tmd.getTableName().equalsIgnoreCase(fk.getReferenceTable()) && exported) {
                    IOHelper.writeInfo("Analizing 2 pass exported fk " + fk.getConstraintName() + " from " + referenceHist + "->" + tmd.getTableName());
                    exportedFKsAnalized.add(fk);
                    TableMetaData fkMetaData = analizeTable(tmd.getSchema(), fk.getReferenceTable());
                    fkMetaData.setType(TableMetaData.MD_FROM_EXPORTED_KEY);
                    findReferences(fkMetaData, importedFKsAnalized, exportedFKsAnalized, tmd.getTableName(), referenceHist + "->" + tmd.getTableName(), true, true);
                    ref2Pass.add(fkMetaData);
                } else {
                    IOHelper.writeInfo(fk.getConstraintName() + " already analized");
                }
            }
            tmd.getReferences().addAll(ref2Pass);
            tmd.getReferencesExported().addAll(ref2Pass);
        }
        tableMetaData.getReferences().addAll(references);
        tableMetaData.getReferencesImported().addAll(importedReferences);
        tableMetaData.getReferencesExported().addAll(exportedReferences);
    }
