    @Override
    public void run() {
        try {
            WikiPagePath path = PathParser.parse(name);
            PageCrawler crawler = context.root.getPageCrawler();
            crawler.setDeadEndStrategy(new VirtualEnabledPageCrawler());
            WikiPage suiteRoot = crawler.getPage(context.root, path);
            if (!suiteRoot.getData().hasAttribute("Suite")) {
                throw new IllegalArgumentException("page " + name + " is not a suite");
            }
            WikiPage root = crawler.getPage(context.root, PathParser.parse("."));
            List<WikiPage> pages = new SuiteContentsFinder(suiteRoot, null, root).makePageList();
            PageListSetUpTearDownSurrounder surrounder = new PageListSetUpTearDownSurrounder(root);
            surrounder.surroundGroupsOfTestPagesWithRespectiveSetUpAndTearDowns(pages);
            for (WikiPage page : pages) {
                if (selects(page)) {
                    String testName = crawler.getFullPath(page).toString();
                    String content = ParallelFitNesseRepository.formatWikiPage(testName, page, null, null, context);
                    queue.add(new InMemoryTestImpl(testName, content));
                }
            }
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            printWriter.close();
            queue.add(new InMemoryTestImpl("Exception", "error reading suite " + name + ": " + e + "\n" + writer.toString()));
        } finally {
            queue.add(ParallelFitNesseRepository.TEST_SENTINEL);
        }
    }
