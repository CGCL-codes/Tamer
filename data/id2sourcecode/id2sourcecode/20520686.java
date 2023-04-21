    private List<Map<String, String>> splitByGblocks(Map<String, String> sequences) {
        updateJob("splitting alignment along conserved blocks (GBlocks stage)");
        CGblocksAnalysis analysis = this.analysisService.gblocks(sequences, this.params.getGblocks());
        CSimpleLocation location = analysis.getLocation();
        List<Map<String, String>> subalignments = new ArrayList<Map<String, String>>();
        List<Integer> columns = new ArrayList<Integer>();
        for (CSimpleLocation.SubLocation sublocation : location.getSublocations()) {
            int start = sublocation.getStart();
            int end = sublocation.getEnd();
            int column = start + (end - start) / 2;
            columns.add(column);
        }
        System.out.println("location=" + location);
        System.out.println("columns=" + columns);
        int lastcolumn = 0;
        for (int column : columns) {
            Map<String, String> subalignment = new LinkedHashMap<String, String>();
            subalignments.add(subalignment);
            for (Map.Entry<String, String> entry : sequences.entrySet()) {
                String accession = entry.getKey();
                String sequence = sequences.get(accession);
                if (column >= sequence.length()) sequence = sequence.substring(lastcolumn); else sequence = sequence.substring(lastcolumn, column);
                subalignment.put(accession, sequence);
            }
            lastcolumn = column;
        }
        Map<String, String> subalignment = new LinkedHashMap<String, String>();
        subalignments.add(subalignment);
        for (Map.Entry<String, String> entry : sequences.entrySet()) {
            String accession = entry.getKey();
            String sequence = entry.getValue().substring(lastcolumn);
            subalignment.put(accession, sequence);
        }
        return subalignments;
    }
