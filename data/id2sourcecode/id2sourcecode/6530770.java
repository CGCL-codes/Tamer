    public int readOutlineFileMetadata(Object outlineObject, PdfObjectReader currentPdfFile, PageLookup pageLookup) {
        LogWriter.writeMethod("{readOutlineFileMetadata " + outlineObject + "}", 0);
        int count = 0;
        String startObj, nextObj, endObj, rawDest, title;
        Map values;
        if (outlineObject instanceof String) values = currentPdfFile.readObject((String) outlineObject, false, null); else values = (Map) outlineObject;
        Object rawNumber = values.get("Count");
        if (rawNumber != null) count = Integer.parseInt(currentPdfFile.getValue((String) rawNumber));
        startObj = (String) values.get("First");
        if (startObj != null) {
            Element root = OutlineDataXML.createElement("root");
            OutlineDataXML.appendChild(root);
            int level = 0;
            readOutlineLevel(root, currentPdfFile, pageLookup, startObj, level);
        }
        return count;
    }
