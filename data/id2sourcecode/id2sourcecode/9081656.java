    private void loadCitations() throws Exception {
        p("doing citations");
        URL url = this.getClass().getResource("/PainterDemoSet.java");
        Scanner scanner = new Scanner(new InputStreamReader(url.openStream()));
        scanner.useDelimiter(".*\\$startcite.*");
        while (scanner.hasNext()) {
            String cite = scanner.next();
            if (cite.contains("$name-")) {
                String[] ret = regexSearch(cite, "\\$name-(.*?)-(.*)\\$endcite");
                citeMap.put(ret[1], ret[2]);
            }
        }
    }
