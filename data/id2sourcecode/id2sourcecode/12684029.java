        private void startRootElement(final String qName, final Attributes attributes) throws SAXException, ObjectDescriptionException {
            if (qName.equals(ClassModelTags.INCLUDE_TAG)) {
                if (this.isInclude) {
                    Log.warn("Ignored nested include tag.");
                    return;
                }
                final String src = attributes.getValue(ClassModelTags.SOURCE_ATTR);
                try {
                    final URL url = new URL(this.resource, src);
                    startIncludeHandling(url);
                    parseXmlDocument(url, true);
                    endIncludeHandling();
                } catch (Exception ioe) {
                    throw new ElementDefinitionException(ioe, "Unable to include file from " + src);
                }
            } else if (qName.equals(ClassModelTags.OBJECT_TAG)) {
                setState(IN_OBJECT);
                final String className = attributes.getValue(ClassModelTags.CLASS_ATTR);
                String register = attributes.getValue(ClassModelTags.REGISTER_NAMES_ATTR);
                if (register != null && register.length() == 0) {
                    register = null;
                }
                final boolean ignored = "true".equals(attributes.getValue(ClassModelTags.IGNORE_ATTR));
                if (!startObjectDefinition(className, register, ignored)) {
                    setState(IGNORE_OBJECT);
                }
            } else if (qName.equals(ClassModelTags.MANUAL_TAG)) {
                final String className = attributes.getValue(ClassModelTags.CLASS_ATTR);
                final String readHandler = attributes.getValue(ClassModelTags.READ_HANDLER_ATTR);
                final String writeHandler = attributes.getValue(ClassModelTags.WRITE_HANDLER_ATTR);
                handleManualMapping(className, readHandler, writeHandler);
            } else if (qName.equals(ClassModelTags.MAPPING_TAG)) {
                setState(MAPPING_STATE);
                final String typeAttr = attributes.getValue(ClassModelTags.TYPE_ATTR);
                final String baseClass = attributes.getValue(ClassModelTags.BASE_CLASS_ATTR);
                startMultiplexMapping(baseClass, typeAttr);
            }
        }
