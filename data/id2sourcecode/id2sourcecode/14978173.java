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
