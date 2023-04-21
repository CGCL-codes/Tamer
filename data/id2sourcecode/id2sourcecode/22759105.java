    private void process(File file, File outDir) throws IOException {
        Scanner scan = new Scanner(file, "iso-8859-1");
        List<String> lines = new ArrayList<String>();
        while (scan.hasNextLine()) {
            lines.add(scan.nextLine());
        }
        System.out.println("readed lines: " + lines.size());
        List<MetaStackTrace> tracesList = this.parceTraces(lines);
        lines = null;
        System.out.println("readed traces: " + tracesList.size());
        {
            for (File traceFile : outDir.listFiles()) {
                if (traceFile.isFile() && traceFile.getName().startsWith("exception") && traceFile.getName().endsWith(".html")) {
                    traceFile.delete();
                }
            }
            for (MetaStackTrace meta : tracesList) {
                PrintStream out = new PrintStream(new File(outDir, "exception-at-log-line-" + meta.getId() + ".html"));
                out.println("<pre>");
                for (String line : meta.getLines()) {
                    out.println(line);
                }
                out.println("</pre>");
                out.close();
            }
        }
        {
            PrintStream out = new PrintStream(new File(outDir, "index.html"));
            Scanner prefix = new Scanner(new File("template_index_prefix.html"));
            while (prefix.hasNextLine()) {
                out.println(prefix.nextLine());
            }
            {
                out.append("Total exceptions: " + tracesList.size() + "<br/>\n");
                out.append("<ul id='toc' class='filetree'>\n");
                for (MetaStackTrace meta : tracesList) {
                    if (meta.isHidden() == false) {
                        out.append("<li class='").append(meta.getFilteredFriends().size() > 0 ? "folder" : "file").append("'>\n");
                        out.append(meta.toHtmlAnchor());
                        if (meta.getFilteredFriends().size() > 0) {
                            out.append("<ul>\n");
                            for (Weight<MetaStackTrace> w : meta.getFilteredFriends()) {
                                out.append("<li class='file'>\n");
                                out.append(w.getRef().toHtmlAnchor() + " (distance=" + w.getWeight() + ")");
                                out.append("</li>\n");
                            }
                            out.append("</ul>\n");
                        }
                        out.append("</li>\n");
                    }
                }
                out.append("</ul>\n");
            }
            Scanner postfix = new Scanner(new File("template_index_postfix.html"));
            while (postfix.hasNextLine()) {
                out.println(postfix.nextLine());
            }
            out.close();
        }
    }
