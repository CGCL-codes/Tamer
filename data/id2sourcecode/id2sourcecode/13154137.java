    public void writeStartingTree(PartitionTreeModel model, XMLWriter writer) {
        setModelPrefix(model.getPrefix());
        Parameter rootHeight = model.getParameter("treeModel.rootHeight");
        switch(model.getStartingTreeType()) {
            case USER:
                if (model.isNewick()) {
                    writeNewickTree(model.getUserStartingTree(), writer);
                } else {
                    writeSimpleTree(model.getUserStartingTree(), writer);
                }
                break;
            case UPGMA:
                writer.writeComment("Construct a rough-and-ready UPGMA tree as an starting tree");
                if (rootHeight.priorType != PriorType.NONE_TREE_PRIOR) {
                    writer.writeOpenTag(UPGMATreeParser.UPGMA_TREE, new Attribute[] { new Attribute.Default<String>(XMLParser.ID, modelPrefix + STARTING_TREE), new Attribute.Default<String>(UPGMATreeParser.ROOT_HEIGHT, "" + rootHeight.initial) });
                } else {
                    writer.writeOpenTag(UPGMATreeParser.UPGMA_TREE, new Attribute[] { new Attribute.Default<String>(XMLParser.ID, modelPrefix + STARTING_TREE) });
                }
                writer.writeOpenTag(DistanceMatrixParser.DISTANCE_MATRIX, new Attribute[] { new Attribute.Default<String>(DistanceMatrixParser.CORRECTION, "JC") });
                writer.writeOpenTag(SitePatternsParser.PATTERNS);
                writer.writeComment("To generate UPGMA starting tree, only use the 1st aligment, " + "which may be 1 of many aligments using this tree.");
                writer.writeIDref(AlignmentParser.ALIGNMENT, options.getDataPartitions(model).get(0).getTaxonList().getId());
                writer.writeCloseTag(SitePatternsParser.PATTERNS);
                writer.writeCloseTag(DistanceMatrixParser.DISTANCE_MATRIX);
                writer.writeCloseTag(UPGMATreeParser.UPGMA_TREE);
                break;
            case RANDOM:
                writer.writeComment("Generate a random starting tree under the coalescent process");
                ClockModelGroup group = options.getDataPartitions(model).get(0).getPartitionClockModel().getClockModelGroup();
                if ((group.getRateTypeOption() == FixRateType.FIX_MEAN || group.getRateTypeOption() == FixRateType.RELATIVE_TO) && model.getDataType().getType() != DataType.MICRO_SAT) {
                    writer.writeOpenTag(CoalescentSimulatorParser.COALESCENT_TREE, new Attribute[] { new Attribute.Default<String>(XMLParser.ID, modelPrefix + STARTING_TREE), new Attribute.Default<String>(CoalescentSimulatorParser.ROOT_HEIGHT, "" + rootHeight.initial) });
                } else {
                    writer.writeOpenTag(CoalescentSimulatorParser.COALESCENT_TREE, new Attribute[] { new Attribute.Default<String>(XMLParser.ID, modelPrefix + STARTING_TREE) });
                }
                String taxaId;
                if (options.hasIdenticalTaxa() && options.getPartitionPattern().size() < 1) {
                    taxaId = TaxaParser.TAXA;
                } else {
                    taxaId = options.getDataPartitions(model).get(0).getPrefix() + TaxaParser.TAXA;
                }
                writeTaxaRef(taxaId, model, writer);
                writeInitialDemoModelRef(model, writer);
                writer.writeCloseTag(CoalescentSimulatorParser.COALESCENT_TREE);
                break;
            default:
                throw new IllegalArgumentException("Unknown StartingTreeType");
        }
    }
