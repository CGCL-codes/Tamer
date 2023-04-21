    private void addAttrPerNodeTableFor(RfCavity node) {
        MPXAttrPerNodeTable nodeAttrTable;
        nodeAttrTable = new MPXAttrPerNodeTable(mProxy.getScenario(), node, mpxDocument.getModelProxy().getChannelSource());
        nodeSPEView.add(nodeAttrTable);
    }
