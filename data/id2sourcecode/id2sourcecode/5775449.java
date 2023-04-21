    private void writeData(IBaseType dataType, XMLStreamWriter2 writer) throws XMLStreamException {
        DataType data = (DataType) baseType;
        if (data.isSetInputStream()) {
            WriteXMLFromInputStream writeXML4is = new WriteXMLFromInputStream(writer, data.getInputStream(), data.isStream());
            Future<Boolean> futureStream = executorStream.submit(writeXML4is);
            if (!data.isStream()) {
                try {
                    futureStream.get();
                } catch (Exception e) {
                    throw new XMLStreamException("Data input stream can not be write ", e);
                }
            }
        } else if (data.isSetOutputStream()) {
            throw new XMLStreamException("DataType only can write streaming input, its an output stream (only for reading) ");
        } else {
            byte[] byData = baseType.asData();
            writer.writeBinary(byData, 0, byData.length);
        }
    }
