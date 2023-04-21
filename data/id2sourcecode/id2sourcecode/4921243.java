    public void extract(boolean includeLSID) throws IOException {
        long start = System.currentTimeMillis();
        writer.write(reader.readLine());
        if (includeLSID) writer.write("\tLSID");
        writer.write("\n");
        System.out.println("Starting to extract...");
        String line = reader.readLine();
        while (line != null) {
            count++;
            try {
                String[] values = line.split("\t");
                String lsid = null;
                if (values != null && values.length == 9) {
                    String name = values[8];
                    try {
                        NameSearchResult result = searcher.searchForRecord(name, null);
                        if (result != null && result.getMatchType() != MatchType.SEARCHABLE) {
                            lsid = getAustralianLsid(result);
                        }
                    } catch (SearchResultException sre) {
                        if (sre instanceof HomonymException) {
                            List<NameSearchResult> results = ((HomonymException) sre).getResults();
                            if (results != null && results.size() > 0) {
                                lsid = getAustralianLsid((NameSearchResult[]) results.toArray(new NameSearchResult[] {}));
                            }
                        }
                    }
                }
                if (lsid != null) {
                    auCount++;
                    writer.write(line);
                    if (includeLSID) writer.write("\t" + lsid);
                    writer.write("\n");
                }
                if (count % 100000 == 0) printStats(start);
                line = reader.readLine();
            } catch (IOException ie) {
                ie.printStackTrace();
                printStats(start);
                System.exit(-1);
            }
        }
        printStats(start);
        writer.flush();
        writer.close();
    }
