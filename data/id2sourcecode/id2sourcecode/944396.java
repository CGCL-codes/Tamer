    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("At leasts two command line arguments must " + "be specified. ");
            System.out.println("<filename> <term1> ... <termN>");
        }
        String filePath = args[0];
        String[] terms = new String[args.length - 1];
        for (int i = 1, max = args.length; i < max; i++) {
            terms[i - 1] = args[i];
        }
        SwingController controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        JPanel viewerComponentPanel = factory.buildViewerPanel();
        JFrame applicationFrame = new JFrame();
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        applicationFrame.getContentPane().add(viewerComponentPanel);
        controller.getDocumentViewController().setAnnotationCallback(new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));
        controller.getDocumentViewController().setViewType(DocumentViewControllerImpl.ONE_COLUMN_VIEW);
        controller.openDocument(filePath);
        DocumentSearchController searchController = controller.getDocumentSearchController();
        for (String term : terms) {
            searchController.addSearchTerm(term, false, false);
        }
        Document document = controller.getDocument();
        int pageCount = 25;
        if (pageCount > document.getNumberOfPages()) {
            pageCount = document.getNumberOfPages();
        }
        applicationFrame.pack();
        applicationFrame.setVisible(true);
        AnnotationState annotationState = new AnnotationState(Annotation.VISIBLE_RECTANGLE, LinkAnnotation.HIGHLIGHT_INVERT, 1f, BorderStyle.BORDER_STYLE_SOLID, Color.GRAY);
        ArrayList<WordText> foundWords;
        java.util.List<AbstractPageViewComponent> pageComponents = controller.getDocumentViewController().getDocumentViewModel().getPageComponents();
        for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
            foundWords = searchController.searchPage(pageIndex);
            if (foundWords != null) {
                AbstractPageViewComponent pageViewComponent = pageComponents.get(pageIndex);
                for (WordText wordText : foundWords) {
                    LinkAnnotation linkAnnotation = (LinkAnnotation) AnnotationFactory.buildAnnotation(document.getPageTree().getLibrary(), AnnotationFactory.LINK_ANNOTATION, wordText.getBounds().getBounds(), annotationState);
                    org.icepdf.core.pobjects.actions.Action action = createURIAction(document.getPageTree().getLibrary(), "http://www.icepdf.org");
                    linkAnnotation.addAction(action);
                    pageViewComponent.addAnnotation(linkAnnotation);
                }
            }
            searchController.clearSearchHighlight(pageIndex);
        }
    }
