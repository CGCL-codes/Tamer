    public void transcode(Document document, String uri, TranscoderOutput output) throws TranscoderException {
        super.transcode(document, uri, output);
        Document doc;
        if (output.getDocument() == null) {
            DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
            doc = domImpl.createDocument(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_SVG_TAG, null);
        } else doc = output.getDocument();
        SVGGraphics2D svgGenerator = new SVGGraphics2D(SVGGeneratorContext.createDefault(doc), ((Boolean) hints.get(KEY_TEXT_AS_SHAPES)).booleanValue());
        Dimension d = new Dimension();
        d.setSize(width, height);
        svgGenerator.setSVGCanvasSize(d);
        this.root.paint(svgGenerator);
        try {
            OutputStream os = output.getOutputStream();
            if (os != null) {
                svgGenerator.stream(svgGenerator.getRoot(), new OutputStreamWriter(os), false, false);
                return;
            }
            Writer wr = output.getWriter();
            if (wr != null) {
                svgGenerator.stream(svgGenerator.getRoot(), wr, false, false);
                return;
            }
            String outputuri = output.getURI();
            if (outputuri != null) {
                try {
                    URL url = new URL(outputuri);
                    URLConnection urlCnx = url.openConnection();
                    os = urlCnx.getOutputStream();
                    svgGenerator.stream(svgGenerator.getRoot(), new OutputStreamWriter(os), false, false);
                    return;
                } catch (MalformedURLException e) {
                    handler.fatalError(new TranscoderException(e));
                } catch (IOException e) {
                    handler.fatalError(new TranscoderException(e));
                }
            }
        } catch (Exception ex) {
            throw new TranscoderException(ex);
        }
    }
