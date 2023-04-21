    public void testWriteSequenceToSAX() throws XQException {
        XQExpression xqe;
        XQSequence xqs;
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        xqs.close();
        try {
            xqs.writeSequenceToSAX(new DefaultHandler());
            junit.framework.Assert.fail("A-XQS-1.2: closed sequence supports writeSequenceToSAX()");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        xqs.next();
        xqs.getItem();
        try {
            xqs.writeSequenceToSAX(new DefaultHandler());
            junit.framework.Assert.fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e a='Hello world!'/>/@*");
        try {
            xqs.writeSequenceToSAX(new DefaultHandler());
            junit.framework.Assert.fail("A-XQS-21.1: serialization process fails when sequence contains a top-level attribute");
        } catch (XQException xq) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        try {
            xqs.writeSequenceToSAX((ContentHandler) null);
            junit.framework.Assert.fail("A-XQS-24.3: writeSequence accepts a null buffer as first argument");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        TestContentHandler result = new TestContentHandler();
        try {
            xqs.writeSequenceToSAX(result);
        } catch (XQException e) {
            junit.framework.Assert.fail("A-XQS-24.1: writeSequence failed with message: " + e.getMessage());
        }
        junit.framework.Assert.assertTrue("A-XQS-24.1: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result.buffer.toString() + "'", result.buffer.toString().indexOf("<e>Hello world!</e>") != -1);
        xqe.close();
    }
