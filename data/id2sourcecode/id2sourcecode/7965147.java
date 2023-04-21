    @AfterClass
    public static void produceIndexOfCharts() throws IOException {
        File f = new File(cacheRoot, "index.html");
        Writer out = new FileWriter(f);
        out.write("<html><head><title>image cache listing</title></head><body>\n");
        out.write(JS);
        String[] names = cacheRoot.list();
        Arrays.sort(names);
        List<String> l = getChartNames(names);
        out.write("<p>\n");
        for (String n : l) {
            out.write("<a href=\"#" + n + "\">" + n + "</a><br/>\n");
        }
        for (String n : l) {
            out.write("<a name=\"" + n + "\"><b>" + n + "</b></a><br/>\n");
            if (n.equals("math")) {
                out.write("<div id=\"chart_desc\"></div>");
                out.write("<img src=\"" + n + ".png\" border=\"0\" usemap=\"#" + mathPNGName + "\"/>\n");
                out.write("<img src=\"" + n + "_t.png\"/><br/>\n");
                out.write(readContents(new File(cacheRoot, n + ".imap")));
            } else {
                out.write("<img src=\"" + n + ".png\" border=\"0\"/>\n");
                String tname = n + "_t.png";
                if (new File(cacheRoot, tname).exists()) {
                    out.write("<img src=\"" + tname + "\"/>\n");
                }
                out.write("<br/>\n");
            }
        }
        out.write("</p>\n");
        out.write("</body></html>");
        out.close();
    }
