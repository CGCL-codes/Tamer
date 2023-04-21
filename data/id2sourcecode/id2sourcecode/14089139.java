    public void testGetSequenceAsStream() throws XQException {
        XQExpression xqe;
        XQSequence xqs;
        XMLStreamReader xmlReader = null;
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        xqs.close();
        try {
            xqs.getSequenceAsStream();
            junit.framework.Assert.fail("A-XQS-1.2: closed sequence supports getSequenceAsStream()");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e a='Hello world!'/>/@*");
        try {
            xmlReader = xqs.getSequenceAsStream();
            while (xmlReader.hasNext()) xmlReader.next();
            junit.framework.Assert.fail("A-XQS-21.1: serialization process fails when sequence contains a top-level attribute");
        } catch (XQException xq) {
        } catch (XMLStreamException xml) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        xqs.next();
        xqs.getItem();
        try {
            xqs.getSequenceAsStream();
            junit.framework.Assert.fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        try {
            xmlReader = xqs.getSequenceAsStream();
        } catch (XQException e) {
            junit.framework.Assert.fail("A-XQS-22.1: getSequenceAsStream failed with message: " + e.getMessage());
        }
        try {
            junit.framework.Assert.assertNotNull("A-XQS-22.1: getSequenceAsStream returned a null XMLStreamReader", xmlReader);
            junit.framework.Assert.assertEquals("A-XQS-22.1: unexpected first event returned by XMLStreamReader", XMLStreamReader.START_DOCUMENT, xmlReader.getEventType());
            junit.framework.Assert.assertEquals("A-XQS-22.1: unexpected second event returned by XMLStreamReader", XMLStreamReader.START_ELEMENT, xmlReader.next());
            junit.framework.Assert.assertEquals("A-XQS-22.1: unexpected third event returned by XMLStreamReader", XMLStreamReader.CHARACTERS, xmlReader.next());
            junit.framework.Assert.assertEquals("A-XQS-22.1: unexpected fourth event returned by XMLStreamReader", XMLStreamReader.END_ELEMENT, xmlReader.next());
            junit.framework.Assert.assertEquals("A-XQS-22.1: unexpected fifth event returned by XMLStreamReader", XMLStreamReader.END_DOCUMENT, xmlReader.next());
        } catch (XMLStreamException e) {
            junit.framework.Assert.fail("A-XQS-22.1: XMLStreamReader.next() failed with message: " + e.getMessage());
        }
        xqe.close();
    }
