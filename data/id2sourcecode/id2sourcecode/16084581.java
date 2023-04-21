    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        if (rowIndex < this.getRowCount()) {
            String key = columnInfo.get(columnIndex).key;
            if (key.equals("key_cluster")) {
                if (rowIndex < clusterNumberList.get(0)) value = 0; else {
                    int start = 0;
                    int end = clusterNumberList.size() - 1;
                    while (rowIndex < clusterNumberList.get(end - 1)) {
                        if (rowIndex < clusterNumberList.get((start + end) / 2)) end = (start + end) / 2; else start = (start + end) / 2;
                    }
                    value = end;
                }
            } else if (key.equals("key_title")) {
                value = getMoleculeAtRow(rowIndex).getTitle();
            } else if (key.equals("key_svg")) {
                value = svgCache.getSVG(getMoleculeAtRow(rowIndex), null, null, svgObserver[rowIndex]);
            } else if (key.equals("key_smiles")) {
                value = getMoleculeAtRow(rowIndex).getSmiles();
            } else if (key.equals("key_publicbanner")) {
                value = bm.getPublicBanner(getMoleculeAtRow(rowIndex));
            } else if (key.equals("key_privatebanner")) {
                value = bm.getPrivateBanner(getMoleculeAtRow(rowIndex));
            } else if (key.startsWith("key_pd_")) {
                Molecule m;
                Molecule mm = getMoleculeAtRow(rowIndex);
                PropertyDefinition pd = columnInfo.get(columnIndex).propertyDefinition;
                switch(getSortState()) {
                    case READY:
                        if (dataPump.contains(pd, mm)) m = mm; else m = null;
                        break;
                    case SORTING:
                        m = mm;
                        break;
                    default:
                        m = null;
                }
                if (columnInfo.get(columnIndex).type == String.class) {
                    if (m == null) {
                        value = "...";
                    } else {
                        value = m.getStringPropertyValue(pd);
                    }
                } else {
                    if (m == null) value = Double.NaN; else value = m.getNumPropertyValue(pd);
                }
            }
        }
        return value;
    }
