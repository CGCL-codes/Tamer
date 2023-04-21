    public void testWriteSequence_Writer() throws XQException {
        XQExpression xqe;
        XQSequence xqs;
        Properties prop = new Properties();
        prop.setProperty("encoding", "UTF-8");
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        xqs.close();
        try {
            xqs.writeSequence(new StringWriter(), prop);
            junit.framework.Assert.fail("A-XQS-1.2: closed sequence supports writeSequence()");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        xqs.next();
        xqs.getItem();
        try {
            xqs.writeSequence(new StringWriter(), prop);
            junit.framework.Assert.fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e a='Hello world!'/>/@*");
        try {
            xqs.writeSequence(new StringWriter(), prop);
            junit.framework.Assert.fail("A-XQS-21.1: serialization process fails when sequence contains a top-level attribute");
        } catch (XQException xq) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        try {
            xqs.writeSequence((Writer) null, prop);
            junit.framework.Assert.fail("A-XQS-24.3: writeSequence accepts a null buffer as first argument");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        StringWriter result = new StringWriter();
        try {
            xqs.writeSequence(result, prop);
        } catch (XQException e) {
            junit.framework.Assert.fail("A-XQS-24.1: writeSequence failed with message: " + e.getMessage());
        }
        junit.framework.Assert.assertTrue("A-XQS-24.1: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result.toString() + "'", result.toString().indexOf("<e>Hello world!</e>") != -1);
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        StringWriter otherResult = new StringWriter();
        try {
            xqs.writeSequence(otherResult, prop);
        } catch (XQException e) {
            junit.framework.Assert.fail("A-XQS-24.2: writeSequence failed with message: " + e.getMessage());
        }
        junit.framework.Assert.assertEquals("A-XQS-24.2: null properties argument is equivalent to empty properties argument", result.toString(), otherResult.toString());
        xqe.close();
    }
