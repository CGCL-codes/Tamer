    protected void writeSVGToOutput(SVGGraphics2D svgGenerator, Element svgRoot, TranscoderOutput output) throws TranscoderException {
        Document doc = output.getDocument();
        if (doc != null) return;
        XMLFilter xmlFilter = output.getXMLFilter();
        if (xmlFilter != null) {
            handler.fatalError(new TranscoderException("" + ERROR_INCOMPATIBLE_OUTPUT_TYPE));
        }
        try {
            boolean escaped = false;
            if (hints.containsKey(KEY_ESCAPED)) escaped = ((Boolean) hints.get(KEY_ESCAPED)).booleanValue();
            OutputStream os = output.getOutputStream();
            if (os != null) {
                svgGenerator.stream(svgRoot, new OutputStreamWriter(os), false, escaped);
                return;
            }
            Writer wr = output.getWriter();
            if (wr != null) {
                svgGenerator.stream(svgRoot, wr, false, escaped);
                return;
            }
            String uri = output.getURI();
            if (uri != null) {
                try {
                    URL url = new URL(uri);
                    URLConnection urlCnx = url.openConnection();
                    os = urlCnx.getOutputStream();
                    svgGenerator.stream(svgRoot, new OutputStreamWriter(os), false, escaped);
                    return;
                } catch (MalformedURLException e) {
                    handler.fatalError(new TranscoderException(e));
                } catch (IOException e) {
                    handler.fatalError(new TranscoderException(e));
                }
            }
        } catch (IOException e) {
            throw new TranscoderException(e);
        }
        throw new TranscoderException("" + ERROR_INCOMPATIBLE_OUTPUT_TYPE);
    }
