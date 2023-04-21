    public static WikipediaDocument retreiveWikipediaDocument(String location) throws IOException {
        URL url = new URL(location);
        System.out.println("Geting location from wikipedia:" + location);
        InputStream is = url.openStream();
        String html = StreamsUtils.readString(is);
        WikipediaDocument wk = new WikipediaDocument();
        int index = 0;
        int startIndex = 0;
        while ((index = html.indexOf("<p>", startIndex)) > 0) {
            int indexEnd = html.indexOf("</p>", index);
            String paragraph = html.substring(index + 3, indexEnd);
            paragraph = paragraph.replaceAll("<[^>]+>", "");
            wk.getParagraphs().add(paragraph);
            startIndex = index + 3;
        }
        int indexTitle = html.indexOf("<title>");
        int indexTitleEnd = html.indexOf("- Wikipedia");
        wk.setTitle(html.substring(indexTitle + 6, indexTitleEnd));
        is.close();
        return wk;
    }
