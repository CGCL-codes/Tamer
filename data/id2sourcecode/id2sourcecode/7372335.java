    private void addChannel(ArrayList articles) throws DocumentException {
        for (Iterator iter = articles.iterator(); iter.hasNext(); ) {
            ArticleObject element = (ArticleObject) iter.next();
            document.add(new Paragraph());
            if (feeddescs) {
                String title = element.getChannelTitle() + CR + element.getTitle();
                writeArticle(element, title);
            } else {
                writeArticle(element, element.getTitle());
            }
        }
    }
