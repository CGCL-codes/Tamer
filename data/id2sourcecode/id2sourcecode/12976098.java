    private CMLMolecule makeMolecule1() {
        URL url = Util.getResource(CMLAssert.CRYSTAL_EXAMPLES + CMLConstants.U_S + "ci6746_1.cml.xml");
        Document document = null;
        try {
            document = new CMLBuilder().build(url.openStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("should not throw " + e.getMessage());
        }
        CMLMolecule molecule = (CMLMolecule) CMLUtil.getQueryNodes(document, "//" + CMLMolecule.NS, CMLConstants.CML_XPATH).get(0);
        List<Node> scalars = CMLUtil.getQueryNodes(molecule, "//" + CMLScalar.NS, CMLConstants.CML_XPATH);
        for (Node node : scalars) {
            node.detach();
        }
        return molecule;
    }
