    public void print(Doc doc, PrintRequestAttributeSet attributes) throws PrintException {
        if (printing) throw new PrintException("already printing");
        printing = true;
        DocAttributeSet docAtts = doc.getAttributes();
        DocFlavor flavor = doc.getDocFlavor();
        if (flavor == null || (!service.isDocFlavorSupported(flavor))) {
            notifyPrintJobListeners(new PrintJobEvent(this, PrintJobEvent.JOB_FAILED));
            throw new PrintFlavorException("Invalid flavor", new DocFlavor[] { flavor });
        }
        HashAttributeSet mergedAtts = new HashAttributeSet();
        if (attributes != null) mergedAtts.addAll(attributes);
        if (docAtts != null) mergedAtts.addAll(docAtts);
        if (!mergedAtts.containsKey(RequestingUserName.class)) {
            mergedAtts.add(IppPrintService.REQUESTING_USER_NAME);
            requestingUser = IppPrintService.REQUESTING_USER_NAME;
        } else {
            requestingUser = (RequestingUserName) mergedAtts.get(RequestingUserName.class);
        }
        if (!mergedAtts.containsKey(JobName.class)) mergedAtts.add(IppPrintService.JOB_NAME);
        IppResponse response = null;
        try {
            PrinterURI printerUri = service.getPrinterURI();
            String printerUriStr = "http" + printerUri.toString().substring(3);
            URI uri = null;
            try {
                uri = new URI(printerUriStr);
            } catch (URISyntaxException e) {
            }
            IppRequest request = new IppRequest(uri, username, password);
            request.setOperationID((short) OperationsSupported.PRINT_JOB.getValue());
            request.setOperationAttributeDefaults();
            request.addOperationAttribute(printerUri);
            if (mergedAtts != null) {
                request.addAndFilterJobOperationAttributes(mergedAtts);
                request.addAndFilterJobTemplateAttributes(mergedAtts);
            }
            DocumentFormat format = DocumentFormat.createDocumentFormat(flavor);
            request.addOperationAttribute(format);
            String className = flavor.getRepresentationClassName();
            if (className.equals("[B")) {
                request.setData((byte[]) doc.getPrintData());
                response = request.send();
            } else if (className.equals("java.io.InputStream")) {
                InputStream stream = (InputStream) doc.getPrintData();
                request.setData(stream);
                response = request.send();
                stream.close();
            } else if (className.equals("[C")) {
                try {
                    String str = new String((char[]) doc.getPrintData());
                    request.setData(str.getBytes("utf-16"));
                    response = request.send();
                } catch (UnsupportedEncodingException e) {
                    notifyPrintJobListeners(new PrintJobEvent(this, PrintJobEvent.JOB_FAILED));
                    throw new PrintFlavorException("Invalid charset of flavor", e, new DocFlavor[] { flavor });
                }
            } else if (className.equals("java.io.Reader")) {
                try {
                    response = request.send();
                    throw new UnsupportedEncodingException("not supported yet");
                } catch (UnsupportedEncodingException e) {
                    notifyPrintJobListeners(new PrintJobEvent(this, PrintJobEvent.JOB_FAILED));
                    throw new PrintFlavorException("Invalid charset of flavor", e, new DocFlavor[] { flavor });
                }
            } else if (className.equals("java.lang.String")) {
                try {
                    String str = (String) doc.getPrintData();
                    request.setData(str.getBytes("utf-16"));
                    response = request.send();
                } catch (UnsupportedEncodingException e) {
                    notifyPrintJobListeners(new PrintJobEvent(this, PrintJobEvent.JOB_FAILED));
                    throw new PrintFlavorException("Invalid charset of flavor", e, new DocFlavor[] { flavor });
                }
            } else if (className.equals("java.net.URL")) {
                URL url = (URL) doc.getPrintData();
                InputStream stream = url.openStream();
                request.setData(stream);
                response = request.send();
                stream.close();
            } else if (className.equals("java.awt.image.renderable.RenderableImage") || className.equals("java.awt.print.Printable") || className.equals("java.awt.print.Pageable")) {
                throw new PrintException("Not yet supported.");
            } else {
                notifyPrintJobListeners(new PrintJobEvent(this, PrintJobEvent.JOB_FAILED));
                throw new PrintFlavorException("Invalid flavor", new DocFlavor[] { flavor });
            }
            notifyPrintJobListeners(new PrintJobEvent(this, PrintJobEvent.DATA_TRANSFER_COMPLETE));
        } catch (IOException e) {
            throw new PrintException("IOException occured.", e);
        }
        int status = response.getStatusCode();
        if (!(status == IppStatusCode.SUCCESSFUL_OK || status == IppStatusCode.SUCCESSFUL_OK_IGNORED_OR_SUBSTITUED_ATTRIBUTES || status == IppStatusCode.SUCCESSFUL_OK_CONFLICTING_ATTRIBUTES)) {
            notifyPrintJobListeners(new PrintJobEvent(this, PrintJobEvent.JOB_FAILED));
            throw new PrintException("Printing failed - received statuscode " + Integer.toHexString(status));
        } else {
            notifyPrintJobListeners(new PrintJobEvent(this, PrintJobEvent.JOB_COMPLETE));
        }
        List jobAtts = response.getJobAttributes();
        Map jobAttributes = (Map) jobAtts.get(0);
        jobUri = (JobUri) ((HashSet) jobAttributes.get(JobUri.class)).toArray()[0];
        jobId = (JobId) ((HashSet) jobAttributes.get(JobId.class)).toArray()[0];
    }
