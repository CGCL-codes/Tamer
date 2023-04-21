    private TableMetaData analizeNextLayer(List<ForeignKey> nextLayer, TableMetaData metaData, List<String> tablesAnalized, boolean imported) throws Exception {
        List<ForeignKey> currLayer = new ArrayList<ForeignKey>(nextLayer);
        nextLayer.clear();
        for (ForeignKey nextFk : currLayer) {
            TableMetaData mt = null;
            if (!tablesAnalized.contains(nextFk.getConstraintName())) {
                mt = analizeTable(metaData.getSchema(), nextFk.getReferenceTable());
                if (!metaData.getReferences().contains(mt) && !metaData.getTableName().equalsIgnoreCase(mt.getTableName())) {
                    metaData.getReferences().add(mt);
                } else {
                    mt = findMetadata(metaData, nextFk.getReferenceTable());
                }
                tablesAnalized.add(nextFk.getConstraintName());
            } else {
                IOHelper.writeInfo("Table already analized " + nextFk.getReferenceTable());
                mt = findMetadata(metaData, nextFk.getReferenceTable());
            }
            if (nextFk.getType() == ForeignKey.EXPORTED_KEY) {
                IOHelper.writeInfo(mt.getTableName() + " added to exported ref on " + findMetadata(metaData, nextFk.getConstraintTableName()).getTableName());
                if (!mt.getTableName().equalsIgnoreCase(nextFk.getConstraintTableName())) findMetadata(metaData, nextFk.getConstraintTableName()).getReferencesExported().add(mt);
                mt.setType(TableMetaData.MD_FROM_EXPORTED_KEY);
            } else {
                IOHelper.writeInfo(mt.getTableName() + " added to imported ref on " + findMetadata(metaData, nextFk.getConstraintTableName()).getTableName());
                if (!mt.getTableName().equalsIgnoreCase(nextFk.getConstraintTableName())) findMetadata(metaData, nextFk.getConstraintTableName()).getReferencesImported().add(mt);
                mt.setType(TableMetaData.MD_FROM_IMPORTED_KEY);
            }
            nextLayer.addAll(buildNextLayerList(mt, tablesAnalized, imported));
        }
        IOHelper.writeInfo("End layer");
        if (nextLayer.size() > 0) {
            IOHelper.writeInfo("Begining next layer. Size :" + nextLayer.size());
            IOHelper.writeInfo("Next layer tables:");
            for (ForeignKey table : nextLayer) {
                IOHelper.writeInfo(table.getReferenceTable());
            }
            return analizeNextLayer(nextLayer, metaData, tablesAnalized, imported);
        } else {
            return metaData;
        }
    }
