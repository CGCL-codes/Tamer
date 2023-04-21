    public ProcessorOutput createOutput(String name) {
        ProcessorOutput output = new ProcessorImpl.ProcessorOutputImpl(getClass(), name) {

            public void readImpl(PipelineContext context, ContentHandler contentHandler) {
                try {
                    final byte[] fileContent;
                    {
                        final String NO_FILE = "No file was uploaded";
                        Document requestDocument = readInputAsDOM4J(context, INPUT_REQUEST);
                        PooledXPathExpression expr = XPathCache.getXPathExpression(context, new DocumentWrapper(requestDocument, null), "/request/parameters/parameter[1]/value");
                        Element valueElement = (Element) expr.evaluateSingle();
                        if (valueElement == null) throw new OXFException(NO_FILE);
                        String type = valueElement.attributeValue(XMLConstants.XSI_TYPE_QNAME);
                        if (type == null) throw new OXFException(NO_FILE);
                        if (type.endsWith("anyURI")) {
                            String url = valueElement.getStringValue();
                            InputStream urlInputStream = new URL(url).openStream();
                            byte[] buffer = new byte[1024];
                            ByteArrayOutputStream fileByteArray = new ByteArrayOutputStream();
                            int size;
                            while ((size = urlInputStream.read(buffer)) != -1) fileByteArray.write(buffer, 0, size);
                            urlInputStream.close();
                            fileContent = fileByteArray.toByteArray();
                        } else {
                            fileContent = Base64.decode(valueElement.getStringValue());
                        }
                    }
                    DOMGenerator domGenerator = new DOMGenerator(extractFromXLS(new ByteArrayInputStream(fileContent)));
                    domGenerator.createOutput(OUTPUT_DATA).read(context, contentHandler);
                } catch (XPathException xpe) {
                    throw new OXFException(xpe);
                } catch (IOException e) {
                    throw new OXFException(e);
                }
            }

            private Document extractFromXLS(InputStream inputStream) throws IOException {
                HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(inputStream));
                DocumentFactory factory = DocumentFactory.getInstance();
                final Document resultDocument = factory.createDocument();
                resultDocument.setRootElement(factory.createElement("workbook"));
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    HSSFSheet sheet = workbook.getSheetAt(i);
                    final Element element = factory.createElement("sheet");
                    resultDocument.getRootElement().add(element);
                    XLSUtils.walk(workbook.createDataFormat(), sheet, new XLSUtils.Handler() {

                        public void cell(HSSFCell cell, String sourceXPath, String targetXPath) {
                            if (targetXPath != null) {
                                int cellType = cell.getCellType();
                                String value = null;
                                switch(cellType) {
                                    case HSSFCell.CELL_TYPE_STRING:
                                    case HSSFCell.CELL_TYPE_BLANK:
                                        value = cell.getStringCellValue();
                                        break;
                                    case HSSFCell.CELL_TYPE_NUMERIC:
                                        double doubleValue = cell.getNumericCellValue();
                                        if (((double) ((int) doubleValue)) == doubleValue) {
                                            value = Integer.toString((int) doubleValue);
                                        } else {
                                            value = XMLUtils.removeScientificNotation(doubleValue);
                                        }
                                        break;
                                }
                                if (value == null) throw new OXFException("Unkown cell type " + cellType + " for XPath expression '" + targetXPath + "'");
                                addToElement(element, targetXPath, value);
                            }
                        }
                    });
                }
                return resultDocument;
            }

            private void addToElement(Element element, String xpath, String value) {
                DocumentFactory factory = DocumentFactory.getInstance();
                StringTokenizer elements = new StringTokenizer(xpath, "/");
                while (elements.hasMoreTokens()) {
                    String name = elements.nextToken();
                    if (elements.hasMoreTokens()) {
                        Element child = element.element(name);
                        if (child == null) {
                            child = factory.createElement(name);
                            element.add(child);
                        }
                        element = child;
                    } else {
                        Element child = factory.createElement(name);
                        child.add(factory.createText(value));
                        element.add(child);
                    }
                }
            }
        };
        addOutput(name, output);
        return output;
    }
