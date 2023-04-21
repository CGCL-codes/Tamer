        XMLGenerator(String outputPath) throws ParserConfigurationException {
            mOutputPath = outputPath;
            mDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Node testPackageElem = mDoc.appendChild(mDoc.createElement(TAG_PACKAGE));
            setAttribute(testPackageElem, ATTRIBUTE_NAME_VERSION, ATTRIBUTE_VALUE_VERSION);
            setAttribute(testPackageElem, ATTRIBUTE_NAME_FRAMEWORK, ATTRIBUTE_VALUE_FRAMEWORK);
        }
