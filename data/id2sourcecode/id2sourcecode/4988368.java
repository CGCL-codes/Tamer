    private List<ForeignKey> buildNextLayerList(TableMetaData metaData, List<String> tablesAnalized, boolean imported) throws Exception {
        List<ForeignKey> nextLayer = new ArrayList<ForeignKey>();
        if (imported) {
            for (ForeignKey fk : metaData.getImportedKeys()) {
                IOHelper.writeInfo("Processing " + fk.getConstraintName() + " from " + metaData.getTableName());
                if (!tablesAnalized.contains(fk.getConstraintName()) && metaData.getSchema().equals(fk.getReferenceSchema())) nextLayer.add(fk); else IOHelper.writeInfo("Import pass - imported fk " + fk.getConstraintName().toUpperCase() + "-" + fk.getConstraintTableName() + " from " + fk.getReferenceTable().toUpperCase() + " but was already analized");
            }
        } else {
            for (ForeignKey fk : metaData.getImportedKeys()) {
                IOHelper.writeInfo("Processing " + fk.getConstraintName() + " from " + metaData.getTableName());
                if (!tablesAnalized.contains(fk.getConstraintName()) && metaData.getSchema().equals(fk.getReferenceSchema())) nextLayer.add(fk); else IOHelper.writeInfo("Export pass - imported fk " + fk.getConstraintName().toUpperCase() + "-" + fk.getConstraintTableName() + " from " + fk.getReferenceTable().toUpperCase() + " but was already analized");
            }
            for (ForeignKey fk : metaData.getExportedKeys()) {
                IOHelper.writeInfo("Processing " + fk.getConstraintName() + " from " + metaData.getTableName());
                if (!tablesAnalized.contains(fk.getConstraintName()) && metaData.getSchema().equals(fk.getReferenceSchema())) nextLayer.add(fk); else IOHelper.writeInfo("Export pass - exported pk " + fk.getConstraintName().toUpperCase() + "-" + fk.getConstraintTableName() + " from " + fk.getReferenceTable().toUpperCase() + " but was already analized");
            }
        }
        return nextLayer;
    }
