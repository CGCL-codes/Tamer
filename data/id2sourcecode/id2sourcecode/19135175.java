    public void search(String searchedText) {
        URL helpIndex = getHelpIndexPageURL();
        String[] searchedWords = searchedText.split("\\s");
        for (int i = 0; i < searchedWords.length; i++) {
            searchedWords[i] = searchedWords[i].toLowerCase().trim();
        }
        List<HelpDocument> helpDocuments = searchInHelpDocuments(helpIndex, searchedWords);
        final StringBuilder htmlText = new StringBuilder("<html><head><meta http-equiv='content-type' content='text/html;charset=UTF-8'><link href='" + new ResourceURLContent(HelpController.class, "resources/help/help.css").getURL() + "' rel='stylesheet'></head><body bgcolor='#ffffff'>\n" + "<div id='banner'><div id='helpheader'>" + "  <a class='bread' href='" + helpIndex + "'> " + this.preferences.getLocalizedString(HelpController.class, "helpTitle") + "</a>" + "</div></div>" + "<div id='mainbox' align='left'>" + "  <table width='100%' border='0' cellspacing='0' cellpadding='0'>" + "    <tr valign='bottom' height='32'>" + "      <td width='3' height='32'>&nbsp;</td>" + "      <td width='32' height='32'><img src='" + new ResourceURLContent(HelpController.class, "resources/help/images/sweethome3dIcon32.png").getURL() + "' height='32' width='32'></td>" + "      <td width='8' height='32'>&nbsp;&nbsp;</td>" + "      <td valign='bottom' height='32'><font id='topic'>" + this.preferences.getLocalizedString(HelpController.class, "searchResult") + "</font></td>" + "    </tr>" + "    <tr height='10'><td colspan='4' height='10'>&nbsp;</td></tr>" + "  </table>" + "  <table width='100%' border='0' cellspacing='0' cellpadding='3'>");
        if (helpDocuments.size() == 0) {
            String searchNotFound = this.preferences.getLocalizedString(HelpController.class, "searchNotFound", searchedText);
            htmlText.append("<tr><td><p>" + searchNotFound + "</td></tr>");
        } else {
            String searchFound = this.preferences.getLocalizedString(HelpController.class, "searchFound", searchedText);
            htmlText.append("<tr><td colspan='2'><p>" + searchFound + "</td></tr>");
            URL searchRelevanceImage = new ResourceURLContent(HelpController.class, "resources/searchRelevance.gif").getURL();
            for (HelpDocument helpDocument : helpDocuments) {
                htmlText.append("<tr><td valign='middle' nowrap><a href='" + helpDocument.getBase() + "'>" + helpDocument.getTitle() + "</a></td><td valign='middle'>");
                for (int i = 0; i < helpDocument.getRelevance() && i < 50; i++) {
                    htmlText.append("<img src='" + searchRelevanceImage + "' width='4' height='12'>");
                }
                htmlText.append("</td></tr>");
            }
        }
        htmlText.append("</table></div></body></html>");
        try {
            showPage(new URL(null, "string://" + htmlText.hashCode(), new URLStreamHandler() {

                @Override
                protected URLConnection openConnection(URL url) throws IOException {
                    return new URLConnection(url) {

                        @Override
                        public void connect() throws IOException {
                        }

                        @Override
                        public InputStream getInputStream() throws IOException {
                            return new ByteArrayInputStream(htmlText.toString().getBytes("UTF-8"));
                        }
                    };
                }
            }));
        } catch (MalformedURLException ex) {
        }
    }
