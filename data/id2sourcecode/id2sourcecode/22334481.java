    public static XmlElement getImportContainingTypeDefinition(WsdlDefinitions definitions, QName paramType) {
        XmlElement types = definitions.getTypes();
        XmlElement returnType = null;
        Iterable<XmlElement> schemas = types.elements(WSConstants.XSD_NS, WSConstants.SCHEMA_TAG);
        for (XmlElement schema : schemas) {
            Iterable<XmlElement> imports = schema.elements(WSConstants.XSD_NS, WSConstants.IMPORT_TAG);
            for (XmlElement importEle : imports) {
                String schemaLocation = importEle.attributeValue(WSConstants.SCHEMA_LOCATION_ATTRIBUTE);
                if (null != schemaLocation && !"".equals(schemaLocation)) {
                    try {
                        URL url = new URL(schemaLocation);
                        URLConnection connection = url.openConnection();
                        connection.connect();
                        XmlElement importedSchema = xsul5.XmlConstants.BUILDER.parseFragmentFromInputStream(connection.getInputStream());
                        returnType = findTypeInSchema(paramType, importedSchema);
                        if (returnType != null) {
                            return importEle;
                        }
                    } catch (MalformedURLException e) {
                        throw new XBayaRuntimeException(e);
                    } catch (XmlBuilderException e) {
                        throw new XBayaRuntimeException(e);
                    } catch (IOException e) {
                        throw new XBayaRuntimeException(e);
                    }
                }
            }
        }
        return null;
    }
