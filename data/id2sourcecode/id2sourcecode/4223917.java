    private void addAttrPerNodeTableFor(Magnet node) {
        MPXAttrPerNodeTable nodeAttrTable;
        nodeAttrTable = new MPXAttrPerNodeTable(mProxy.getScenario(), node, mpxDocument.getModelProxy().getChannelSource());
        nodeSPEView.add(nodeAttrTable);
    }
