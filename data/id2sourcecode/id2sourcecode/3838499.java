    protected void writeElement(Element elem) {
        if (writeZip) {
            try {
                doc.appendChild(elem);
                ZipEntry entry = new ZipEntry(intToString(entryNumber++, 8) + "_" + elem.getNodeName());
                entry.setMethod(compressionMethod);
                ((ZipOutputStream) outputStream).putNextEntry(entry);
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                trans.transform(new DOMSource(doc), new StreamResult(outputStream));
                doc.removeChild(elem);
            } catch (Exception e) {
                statechum.Helper.throwUnchecked("failed to write out XML ", e);
            }
        } else {
            topElement.appendChild(elem);
            topElement.appendChild(AbstractPersistence.endl(doc));
        }
    }
