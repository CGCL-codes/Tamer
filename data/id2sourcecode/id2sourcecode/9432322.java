    private static int processMIQL(String miql, File output) throws IOException {
        int interactionCount = 0;
        System.out.print("\n" + miql + ": ");
        final FileWriter writer = new FileWriter(output, true);
        int from = 0;
        List<String> lines = null;
        do {
            URL url = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/" + miql + "?firstResult=" + from + "&maxResults=" + PAGE_SIZE);
            final URLConnection con = url.openConnection();
            con.setConnectTimeout(5000);
            final InputStream is = con.getInputStream();
            lines = readLines(is);
            interactionCount += lines.size();
            is.close();
            System.out.print(lines.size() + "  ");
            from += PAGE_SIZE;
            writeLines(lines, writer);
        } while (lines.size() == PAGE_SIZE);
        return interactionCount;
    }
