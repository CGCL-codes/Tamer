    @Override
    public void process() {
        Integer _status_id = null;
        if (StringUtil.exists(http_request.getParameter("status_id"))) _status_id = Integer.valueOf(http_request.getParameter("status_id"));
        List<Abstract> _abstracts = null;
        if (_status_id != null) _abstracts = new AbstractMapper().findAllByProgramAndStatus(program.getId(), _status_id); else _abstracts = new AbstractMapper().findAllByProgram(program.getId());
        if (_abstracts == null) return;
        Document _document = new Document(PageSize.LETTER);
        PdfCopy _writer = null;
        try {
            _writer = new PdfCopy(_document, http_response.getOutputStream());
            _document.open();
        } catch (DocumentException _e) {
            throw new RuntimeException(_e);
        } catch (IOException _e) {
            throw new RuntimeException(_e);
        }
        try {
            for (Abstract _abstract : _abstracts) {
                PdfReader _reader = null;
                if (_abstract.hasAbstractFile()) _reader = new PdfReader(getFileInputStream(_abstract.getAbstractFile())); else {
                    ByteArrayOutputStream _baos = new ByteArrayOutputStream();
                    PDFConverter.abstract2PDF(_abstract, _baos);
                    _reader = new PdfReader(new ByteArrayInputStream(_baos.toByteArray()));
                }
                PdfImportedPage _page = null;
                for (int _i = 1; _i <= _reader.getNumberOfPages(); _i++) {
                    _page = _writer.getImportedPage(_reader, _i);
                    _writer.addPage(_page);
                }
            }
        } catch (IOException _e) {
            throw new RuntimeException(_e);
        } catch (BadPdfFormatException _e) {
            throw new RuntimeException(_e);
        }
        _document.close();
    }
