    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        controller.openDocument(filePath);
        PageTree pageTree = controller.getPageTree();
        PropertyHandler handler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(Activator.PLUGIN_ID, Constants.PROPERTIES_FILE);
        monitor.beginTask(handler.getProperty("_open_pdf_progress_opening"), pageTree.getNumberOfPages());
        List<GeneralTextTerm> results = pdfText.getResults();
        List<SpecificTextTerm> resultsCopy = new ArrayList<SpecificTextTerm>();
        for (GeneralTextTerm r : results) {
            if (r instanceof SpecificTextTerm) resultsCopy.add((SpecificTextTerm) r);
        }
        TextHighlighter highlighter = new TextHighlighter(pageTree, controller);
        try {
            highlighter.highlightResults(resultsCopy, monitor);
        } catch (InterruptedException e) {
            controller.closeDocument();
            throw new InterruptedException(handler.getProperty("_open_pdf_progress_cancel"));
        }
        monitor.done();
    }
