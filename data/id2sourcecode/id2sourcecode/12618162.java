    public String convertToXml(final LoggingEvent event) throws XMLStreamException {
        final StringWriter writerOut = new StringWriter(2048);
        final XMLOutputFactory factory = XMLOutputFactory.newInstance();
        final XMLStreamWriter writer = factory.createXMLStreamWriter(writerOut);
        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("eventRequest");
        writer.writeAttribute("xmlns", NS_URI);
        writer.writeStartElement("host");
        writer.writeCharacters(getLocalHostName());
        writer.writeEndElement();
        writer.writeStartElement("logger");
        writer.writeCharacters(event.getLoggerName());
        writer.writeEndElement();
        writer.writeStartElement("level");
        writer.writeCharacters(event.getLevel().toString());
        writer.writeEndElement();
        writer.writeStartElement("message");
        writer.writeCharacters(event.getRenderedMessage());
        writer.writeEndElement();
        writer.writeStartElement("timestamp");
        writer.writeCharacters(getDateFormat().format(new Date()));
        writer.writeEndElement();
        writer.writeStartElement("thread");
        writer.writeCharacters(Thread.currentThread().getName());
        writer.writeEndElement();
        if (event.getMDC("service.projectName") != null) {
            writer.writeStartElement("project");
            writer.writeCharacters(event.getMDC("service.projectName").toString());
            writer.writeEndElement();
        }
        if (event.getMDC("service.name") != null) {
            writer.writeStartElement("service");
            writer.writeCharacters(event.getMDC("service.name").toString());
            writer.writeEndElement();
        }
        if (event.getMDC("message.correlationId") != null) {
            writer.writeStartElement("correlationId");
            writer.writeCharacters(event.getMDC("message.correlationId").toString());
            writer.writeEndElement();
        }
        if (event.getMDC("message.messageId") != null) {
            writer.writeStartElement("messageId");
            writer.writeCharacters(event.getMDC("message.messageId").toString());
            writer.writeEndElement();
        }
        final LocationInfo locationInfo = event.getLocationInformation();
        if (locationInfo != null) {
            writer.writeStartElement("location");
            if (locationInfo.getFileName() != null) {
                writer.writeStartElement("fileName");
                writer.writeCharacters(locationInfo.getFileName());
                writer.writeEndElement();
            }
            if (locationInfo.getClassName() != null) {
                writer.writeStartElement("className");
                writer.writeCharacters(locationInfo.getClassName());
                writer.writeEndElement();
            }
            if (locationInfo.getMethodName() != null) {
                writer.writeStartElement("methodName");
                writer.writeCharacters(locationInfo.getMethodName());
                writer.writeEndElement();
            }
            if (locationInfo.getLineNumber() != null) {
                writer.writeStartElement("lineNumber");
                writer.writeCharacters(locationInfo.getLineNumber());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        final ThrowableInformation throwableInfo = event.getThrowableInformation();
        if (throwableInfo != null) {
            writer.writeStartElement("exception");
            final String[] detail = event.getThrowableInformation().getThrowableStrRep();
            if (throwableInfo.getClass() != null) {
                writer.writeStartElement("exceptionName");
                writer.writeCharacters(throwableInfo.getThrowable().getClass().getName());
                writer.writeEndElement();
            }
            if (detail.length > 0) {
                writer.writeStartElement("message");
                writer.writeCharacters(detail[0]);
                writer.writeEndElement();
            }
            if (detail.length > 1) {
                writer.writeStartElement("detail");
                for (int i = 1; i < detail.length; i++) {
                    writer.writeCharacters(detail[i]);
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeEndDocument();
        writer.close();
        return writerOut.toString();
    }
