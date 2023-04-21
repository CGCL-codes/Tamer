    public void testWriteSequenceToResult() throws XQException {
        XQExpression xqe;
        XQSequence xqs;
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        xqs.close();
        try {
            xqs.writeSequenceToResult(new StreamResult(new StringWriter()));
            junit.framework.Assert.fail("A-XQS-1.2: closed sequence supports writeSequence()");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        xqs.next();
        xqs.getItem();
        try {
            xqs.writeSequenceToResult(new StreamResult(new StringWriter()));
            junit.framework.Assert.fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        try {
            xqs.writeSequenceToResult((Result) null);
            junit.framework.Assert.fail("A-XQS-24.3: writeSequence accepts a null buffer as first argument");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        StringWriter result = new StringWriter();
        try {
            xqs.writeSequenceToResult(new StreamResult(result));
        } catch (XQException e) {
            junit.framework.Assert.fail("A-XQS-24.1: writeSequence failed with message: " + e.getMessage());
        }
        junit.framework.Assert.assertTrue("A-XQS-24.1: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result.toString() + "'", result.toString().indexOf("<e>Hello world!</e>") != -1);
        xqe.close();
    }
