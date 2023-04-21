    protected Sequence processCompressedEntry(String name, boolean isDirectory, InputStream is, Sequence filterParam, Sequence storeParam) throws IOException, XPathException, XMLDBException {
        String dataType = isDirectory ? "folder" : "resource";
        Sequence filterParams[] = new Sequence[3];
        filterParams[0] = new StringValue(name);
        filterParams[1] = new StringValue(dataType);
        filterParams[2] = filterParam;
        Sequence entryFilterFunctionResult = entryFilterFunction.evalFunction(contextSequence, null, filterParams);
        if (BooleanValue.FALSE == entryFilterFunctionResult.itemAt(0)) {
            return Sequence.EMPTY_SEQUENCE;
        } else {
            Sequence entryDataFunctionResult;
            Sequence uncompressedData = Sequence.EMPTY_SEQUENCE;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int read = -1;
            while ((read = is.read(buf)) != -1) {
                baos.write(buf, 0, read);
            }
            byte[] entryData = baos.toByteArray();
            if (entryDataFunction.getSignature().getArgumentCount() == 3) {
                Sequence dataParams[] = new Sequence[3];
                System.arraycopy(filterParams, 0, dataParams, 0, 2);
                dataParams[2] = storeParam;
                entryDataFunctionResult = entryDataFunction.evalFunction(contextSequence, null, dataParams);
                String path = entryDataFunctionResult.itemAt(0).getStringValue();
                Collection root = new LocalCollection(context.getUser(), context.getBroker().getBrokerPool(), new AnyURIValue("/db").toXmldbURI(), context.getAccessContext());
                if (isDirectory) {
                    XMLDBAbstractCollectionManipulator.createCollection(root, path);
                } else {
                    Resource resource;
                    File file = new File(path);
                    name = file.getName();
                    path = file.getParent();
                    Collection target = (path == null) ? root : XMLDBAbstractCollectionManipulator.createCollection(root, path);
                    MimeType mime = MimeTable.getInstance().getContentTypeFor(name);
                    try {
                        NodeValue content = ModuleUtils.streamToXML(context, new ByteArrayInputStream(baos.toByteArray()));
                        resource = target.createResource(name, "XMLResource");
                        ContentHandler handler = ((XMLResource) resource).setContentAsSAX();
                        handler.startDocument();
                        content.toSAX(context.getBroker(), handler, null);
                        handler.endDocument();
                    } catch (SAXException e) {
                        resource = target.createResource(name, "BinaryResource");
                        resource.setContent(baos.toByteArray());
                    }
                    if (resource != null) {
                        if (mime != null) {
                            ((EXistResource) resource).setMimeType(mime.getName());
                        }
                        target.storeResource(resource);
                    }
                }
            } else {
                try {
                    uncompressedData = ModuleUtils.streamToXML(context, new ByteArrayInputStream(entryData));
                } catch (SAXException saxe) {
                    if (entryData.length > 0) uncompressedData = BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(), new ByteArrayInputStream(entryData));
                }
                Sequence dataParams[] = new Sequence[4];
                System.arraycopy(filterParams, 0, dataParams, 0, 2);
                dataParams[2] = uncompressedData;
                dataParams[3] = storeParam;
                entryDataFunctionResult = entryDataFunction.evalFunction(contextSequence, null, dataParams);
            }
            return entryDataFunctionResult;
        }
    }
