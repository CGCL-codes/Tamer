    @Override
    public Element createXML(Document document, ProcessInfo processInfo) {
        Element element = document.createElement("process");
        if (processInfo instanceof ReadMetadataProcessInfo) {
            ReadMetadataProcessInfo readMetadataProcessInfo = (ReadMetadataProcessInfo) processInfo;
            ElementWriter<ReadMetadataProcessInfo> writer = new ReadMetadataProcessInfoElement();
            Element childElement = writer.createXML(document, readMetadataProcessInfo);
            element.appendChild(childElement);
        } else if (processInfo instanceof GenerationProcessInfo) {
            GenerationProcessInfo generationProcessInfo = (GenerationProcessInfo) processInfo;
            ElementWriter<GenerationProcessInfo> writer = new GenerationProcessInfoElement();
            Element childElement = writer.createXML(document, generationProcessInfo);
            element.appendChild(childElement);
        }
        return element;
    }
