    public void report(String filename, CanonicalGFF cgff) {
        try {
            FileWriter fw = new FileWriter(new File(filename));
            fw.write("#GeneID" + "\t" + "splice" + "\t" + "#reads");
            if (geneModel == null) {
            } else {
                fw.write("\t" + "novel");
            }
            fw.write("\t" + "splicingPosFreq");
            fw.write("\t" + "format:.fineSplice" + "\n");
            for (Iterator iterator = cgff.geneLengthMap.keySet().iterator(); iterator.hasNext(); ) {
                String geneID = (String) iterator.next();
                Map spliceCntMap = (Map) geneSpliceCntMap.get(geneID);
                Map spliceSplicingPosMap = (Map) geneSpliceSplicingPosMap.get(geneID);
                if (spliceCntMap == null) continue;
                Set modelSpliceSet;
                if (geneModel == null) {
                    modelSpliceSet = null;
                } else {
                    modelSpliceSet = (Set) geneSpliceMapByModel.get(geneID);
                }
                for (Iterator spliceIterator = spliceCntMap.keySet().iterator(); spliceIterator.hasNext(); ) {
                    SpliceLocation splice = (SpliceLocation) spliceIterator.next();
                    float uniqCnt = ((Number) spliceCntMap.get(splice)).floatValue();
                    fw.write(geneID + "\t");
                    fw.write(splice.toString() + "\t");
                    fw.write(new Float(uniqCnt).toString() + "\t");
                    if (geneModel == null) {
                    } else {
                        if (modelSpliceSet != null && modelSpliceSet.contains(splice) == false) {
                            fw.write("V" + "\t");
                        } else {
                            fw.write(" " + "\t");
                        }
                    }
                    if (spliceSplicingPosMap.containsKey(splice)) {
                        fw.write(spliceSplicingPosMap.get(splice).toString());
                    } else {
                        fw.write(" ");
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
