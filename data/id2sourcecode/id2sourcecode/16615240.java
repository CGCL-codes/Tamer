    public void report(String filename, CanonicalGFF cgff) {
        try {
            FileWriter fw = new FileWriter(new File(filename));
            if (intronicCGFF) {
                fw.write("#GeneID" + "\t" + "intronNo" + "\t" + "#reads" + "\t" + "intronLen" + "\t" + "multi/ALL" + "\t" + "format:.intronCount" + "\n");
            } else {
                fw.write("#GeneID" + "\t" + "exonNo" + "\t" + "#reads" + "\t" + "exonLen" + "\t" + "multi/ALL" + "\t" + "format:.exonCount" + "\n");
            }
            for (Iterator iterator = cgff.geneLengthMap.keySet().iterator(); iterator.hasNext(); ) {
                String geneID = (String) iterator.next();
                Map uniqReadCntMap = (Map) geneExonCntMapUniq.get(geneID);
                Map multiReadCntMap = (Map) geneExonCntMapMulti.get(geneID);
                Set exonRegions = (Set) cgff.geneExonRegionMap.get(geneID);
                int exonNo = 1;
                for (Iterator exonIterator = exonRegions.iterator(); exonIterator.hasNext(); ) {
                    Interval exonInterval = (Interval) exonIterator.next();
                    float uniqCnt = 0;
                    if (uniqReadCntMap != null && uniqReadCntMap.containsKey(exonInterval)) {
                        uniqCnt = ((Number) uniqReadCntMap.get(exonInterval)).floatValue();
                    }
                    float multiCnt = 0;
                    if (multiReadCntMap != null && multiReadCntMap.containsKey(exonInterval)) {
                        multiCnt = ((Number) multiReadCntMap.get(exonInterval)).floatValue();
                    }
                    fw.write(geneID + "\t");
                    fw.write(exonNo + "\t");
                    fw.write((uniqCnt + multiCnt) + "\t");
                    fw.write(exonInterval.length() + "\t");
                    if ((uniqCnt + multiCnt) > 0) {
                        fw.write(new Float(multiCnt / (uniqCnt + multiCnt)).toString());
                    } else {
                        fw.write("0");
                    }
                    fw.write("\n");
                    exonNo++;
                }
            }
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
