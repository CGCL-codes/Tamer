    public static String getSimpleTableHtmlString(String[] headings, String[] values) {
        String table = "<table class=\"details\" border=\"0\" cellpadding=\"5\" cellspacing=\"2\" width=\"95%\">\n";
        String row = "";
        if (headings.length > 0) {
            row = "<tr valign=\"top\">\n";
            String key = headings[0];
            String[] tmpHeadings = new String[headings.length - 1];
            for (int i = 0; i < tmpHeadings.length; i++) {
                tmpHeadings[i] = headings[i + 1];
            }
            table = table + getSimpleNormalHeadingHtml(key, tmpHeadings) + "\n";
            row = row + "<td  rowspan=\"1\" valign=\"top\"> " + values[0] + " </td>\n";
            for (int i = 1; i < headings.length; i++) {
                row = row + "<td>" + values[i] + "  </td>\n";
            }
            row = row + "</tr>\n";
        }
        table = table + row + "</table> \n";
        return table;
    }
