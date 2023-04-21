    public void report(String filename, CanonicalGFF cgff) {
        try {
            FileWriter fw = new FileWriter(new File(filename));
            fw.write("#GeneID" + "\t" + "exonPair" + "\t" + "#reads" + "\t" + "jumping");
            if (geneModel == null) {
            } else {
                fw.write("\t" + "novel");
            }
            fw.write("\t" + "splicingPosFreq");
            fw.write("\t" + "format:.spliceCount" + "\n");
            for (Iterator iterator = cgff.geneLengthMap.keySet().iterator(); iterator.hasNext(); ) {
                String geneID = (String) iterator.next();
                Map spliceCntMap = (Map) geneSpliceCntMap.get(geneID);
                Map exonPairSplicePosMap = (Map) geneExonPairSplicingPosMap.get(geneID);
                if (spliceCntMap == null) continue;
                Set modelSpliceSet;
                if (geneModel == null) {
                    modelSpliceSet = null;
                } else {
                    modelSpliceSet = (Set) geneSpliceMapByModel.get(geneID);
                }
                for (Iterator exonPairIterator = spliceCntMap.keySet().iterator(); exonPairIterator.hasNext(); ) {
                    Interval exonPair = (Interval) exonPairIterator.next();
                    float uniqCnt = ((Number) spliceCntMap.get(exonPair)).floatValue();
                    fw.write(geneID + "\t");
                    fw.write(exonPair.getStart() + "<=>" + exonPair.getStop() + "\t");
                    fw.write(new Float(uniqCnt).toString() + "\t");
                    if (exonPair.getStop() - exonPair.getStart() > 1) {
                        fw.write("V");
                    } else {
                        fw.write(" ");
                    }
                    if (geneModel == null) {
                    } else {
                        if (modelSpliceSet != null && modelSpliceSet.contains(exonPair) == false) {
                            fw.write("\t" + "V");
                        } else {
                            fw.write("\t" + " ");
                        }
                    }
                    if (exonPairSplicePosMap.containsKey(exonPair)) {
                        fw.write("\t" + exonPairSplicePosMap.get(exonPair));
                    } else {
                        fw.write("\t" + " ");
                    }
                    fw.write("\n");
                }
            }
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
