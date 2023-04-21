    protected GraphicsNode createImageGraphicsNode(BridgeContext ctx, Element e, ParsedURL purl) {
        Rectangle2D bounds = getImageBounds(ctx, e);
        if ((bounds.getWidth() == 0) || (bounds.getHeight() == 0)) {
            ShapeNode sn = new ShapeNode();
            sn.setShape(bounds);
            return sn;
        }
        SVGDocument svgDoc = (SVGDocument) e.getOwnerDocument();
        String docURL = svgDoc.getURL();
        ParsedURL pDocURL = null;
        if (docURL != null) pDocURL = new ParsedURL(docURL);
        UserAgent userAgent = ctx.getUserAgent();
        try {
            userAgent.checkLoadExternalResource(purl, pDocURL);
        } catch (SecurityException ex) {
            throw new BridgeException(e, ERR_URI_UNSECURE, new Object[] { purl });
        }
        DocumentLoader loader = ctx.getDocumentLoader();
        ImageTagRegistry reg = ImageTagRegistry.getRegistry();
        ICCColorSpaceExt colorspace = extractColorSpace(e, ctx);
        {
            try {
                Document doc = loader.checkCache(purl.toString());
                if (doc != null) {
                    imgDocument = (SVGDocument) doc;
                    return createSVGImageNode(ctx, e, imgDocument);
                }
            } catch (BridgeException ex) {
                throw ex;
            } catch (Exception ex) {
            }
            Filter img = reg.checkCache(purl, colorspace);
            if (img != null) {
                return createRasterImageNode(ctx, e, img);
            }
        }
        ProtectedStream reference = null;
        try {
            reference = openStream(e, purl);
        } catch (SecurityException ex) {
            throw new BridgeException(e, ERR_URI_UNSECURE, new Object[] { purl });
        } catch (IOException ioe) {
            return createBrokenImageNode(ctx, e, purl.toString());
        }
        {
            Filter img = reg.readURL(reference, purl, colorspace, false, false);
            if (img != null) {
                return createRasterImageNode(ctx, e, img);
            }
        }
        try {
            reference.retry();
        } catch (IOException ioe) {
            try {
                reference = openStream(e, purl);
            } catch (IOException ioe2) {
                return createBrokenImageNode(ctx, e, purl.toString());
            }
        }
        try {
            Document doc = loader.loadDocument(purl.toString(), reference);
            imgDocument = (SVGDocument) doc;
            return createSVGImageNode(ctx, e, imgDocument);
        } catch (BridgeException ex) {
            throw ex;
        } catch (SecurityException ex) {
            throw new BridgeException(e, ERR_URI_UNSECURE, new Object[] { purl });
        } catch (Exception ex) {
        }
        try {
            reference.retry();
        } catch (IOException ioe) {
            try {
                reference = openStream(e, purl);
            } catch (IOException ioe2) {
                return createBrokenImageNode(ctx, e, purl.toString());
            }
        }
        try {
            Filter img = reg.readURL(reference, purl, colorspace, true, true);
            if (img != null) {
                return createRasterImageNode(ctx, e, img);
            }
        } finally {
            reference.release();
        }
        return null;
    }
