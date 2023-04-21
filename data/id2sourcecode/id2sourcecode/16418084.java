    public static Result download(String pubmedID) throws Exception {
        InputStream inputStream = null;
        try {
            URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&id=" + pubmedID);
            inputStream = new BufferedInputStream(url.openStream());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            Element root = document.getDocumentElement();
            Result result = new Result();
            Element pubmedArticle = getElement(root, "PubmedArticle");
            Element article = getElement(pubmedArticle, "Article");
            result.title = getText(article, "ArticleTitle");
            Element abstractElement = getElement(article, "Abstract");
            if (abstractElement != null) {
                result.abstractText = getText(abstractElement, "AbstractText");
            }
            result.authors = new ArrayList<String>();
            NodeList authors = article.getElementsByTagName("Author");
            for (int i = 0; i < authors.getLength(); i++) {
                Element author = (Element) authors.item(i);
                String lastName = getText(author, "LastName");
                String initials = getText(author, "Initials");
                String collectiveName = getText(author, "CollectiveName");
                if ((lastName != null) && (initials != null)) {
                    result.authors.add(lastName + " " + initials);
                } else if (collectiveName != null) {
                    result.authors.add(collectiveName);
                }
            }
            Element journal = getElement(article, "Journal");
            Element book = getElement(article, "Book");
            if (journal != null) {
                result.journalTitle = getText(journal, "Title");
                Element journalIssue = getElement(journal, "JournalIssue");
                if (journalIssue != null) {
                    result.journalVolume = getText(journalIssue, "Volume");
                    result.journalIssue = getText(journalIssue, "Issue");
                    result.journalPubDate = parsePubDate(journalIssue, "PubDate");
                }
            } else if (book != null) {
                result.bookTitle = getText(book, "Title");
                result.bookPublisher = getText(book, "Publisher");
                result.bookPubDate = parsePubDate(book, "PubDate");
            }
            return result;
        } catch (Exception exception) {
            throw new Exception("Exception downloading PubMed data", exception);
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }
