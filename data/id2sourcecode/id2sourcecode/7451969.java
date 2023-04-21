    @Test
    public void generate() throws Exception {
        ApplicationContextManager.getApplicationContext();
        IDataBase dataBase = ApplicationContextManager.getBean(IDataBase.class);
        File wordsFile = FileUtilities.findFileRecursively(new File("."), "english.txt");
        String wordsData = FileUtilities.getContents(wordsFile.toURI().toURL().openStream(), Short.MAX_VALUE).toString();
        StringTokenizer stringTokenizer = new StringTokenizer(wordsData);
        List<String> words = new ArrayList<String>();
        while (stringTokenizer.hasMoreTokens()) {
            words.add(stringTokenizer.nextToken());
        }
        Collections.shuffle(words);
        int index = 0;
        for (String word : words) {
            String newSearchUrl = searchUrl + word;
            logger.info("Search url : " + newSearchUrl);
            URL url = new URL(newSearchUrl);
            String contents = FileUtilities.getContents(url.openStream(), Integer.MAX_VALUE).toString();
            Element totalNumberOfResultsElement = XmlUtilities.getElement(XmlUtilities.getDocument(new ByteArrayInputStream(contents.getBytes()), IConstants.ENCODING).getRootElement(), "totalNumberOfResults");
            int totalResults = Integer.parseInt(totalNumberOfResultsElement.getText());
            DataGeneratorMedical dataGenerator = new DataGeneratorMedical(dataBase);
            dataGenerator.before();
            dataGenerator.setInputStream(new ByteArrayInputStream(contents.getBytes()));
            dataGenerator.generate();
            if (totalResults > 20) {
                int offset = 0;
                while (offset < totalResults) {
                    String pagingSearchUrl = newSearchUrl + "&offset=" + offset;
                    logger.info("Paging search url : " + pagingSearchUrl);
                    offset += 20;
                    url = new URL(pagingSearchUrl);
                    dataGenerator.setInputStream(url.openStream());
                    dataGenerator.generate();
                }
            }
            if (index >= 100) {
                break;
            }
        }
    }
