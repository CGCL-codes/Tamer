    @Override
    public boolean newPage() {
        lastElementType = -1;
        if (isPageEmpty()) {
            setNewPageSizeAndMargins();
            return false;
        }
        if (!open || close) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("the.document.is.not.open"));
        }
        PdfPageEvent pageEvent = writer.getPageEvent();
        if (pageEvent != null) pageEvent.onEndPage(writer, this);
        super.newPage();
        indentation.imageIndentLeft = 0;
        indentation.imageIndentRight = 0;
        try {
            flushLines();
            int rotation = pageSize.getRotation();
            if (writer.isPdfX()) {
                if (thisBoxSize.containsKey("art") && thisBoxSize.containsKey("trim")) throw new PdfXConformanceException(MessageLocalization.getComposedMessage("only.one.of.artbox.or.trimbox.can.exist.in.the.page"));
                if (!thisBoxSize.containsKey("art") && !thisBoxSize.containsKey("trim")) {
                    if (thisBoxSize.containsKey("crop")) thisBoxSize.put("trim", thisBoxSize.get("crop")); else thisBoxSize.put("trim", new PdfRectangle(pageSize, pageSize.getRotation()));
                }
            }
            pageResources.addDefaultColorDiff(writer.getDefaultColorspace());
            if (writer.isRgbTransparencyBlending()) {
                PdfDictionary dcs = new PdfDictionary();
                dcs.put(PdfName.CS, PdfName.DEVICERGB);
                pageResources.addDefaultColorDiff(dcs);
            }
            PdfDictionary resources = pageResources.getResources();
            PdfPage page = new PdfPage(new PdfRectangle(pageSize, rotation), thisBoxSize, resources, rotation);
            page.put(PdfName.TABS, writer.getTabs());
            page.putAll(writer.getPageDictEntries());
            writer.resetPageDictEntries();
            if (pageAA != null) {
                page.put(PdfName.AA, writer.addToBody(pageAA).getIndirectReference());
                pageAA = null;
            }
            if (annotationsImp.hasUnusedAnnotations()) {
                PdfArray array = annotationsImp.rotateAnnotations(writer, pageSize);
                if (array.size() != 0) page.put(PdfName.ANNOTS, array);
            }
            if (writer.isTagged()) page.put(PdfName.STRUCTPARENTS, new PdfNumber(writer.getCurrentPageNumber() - 1));
            if (text.size() > textEmptySize) text.endText(); else text = null;
            writer.add(page, new PdfContents(writer.getDirectContentUnder(), graphics, text, writer.getDirectContent(), pageSize));
            initPage();
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        return true;
    }
