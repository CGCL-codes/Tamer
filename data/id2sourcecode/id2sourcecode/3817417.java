    @Test
    public void addingALinkProvider() throws IOException, DocumentException {
        Document doc = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File("./target/test-classes/examples/columbus3.pdf")));
        doc.open();
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setLinkProvider(new LinkProvider() {

            public String getLinkRoot() {
                return "http://www.gutenberg.org/dirs/1/8/0/6/18066/18066-h/";
            }
        }).setTagFactory(Tags.getHtmlTagProcessorFactory());
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(doc, writer)));
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(worker);
        p.parse(XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"));
        doc.close();
    }
