    public void testGetSequenceAsString() throws XQException {
        XQExpression xqe;
        XQSequence xqs;
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        xqs.close();
        try {
            xqs.getSequenceAsString(new Properties());
            junit.framework.Assert.fail("A-XQS-1.2: closed sequence supports getSequenceAsString()");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e a='Hello world!'/>/@*");
        try {
            xqs.getSequenceAsString(new Properties());
            junit.framework.Assert.fail("A-XQS-21.1: serialization process fails when sequence contains a top-level attribute");
        } catch (XQException xq) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        xqs.next();
        xqs.getItem();
        try {
            xqs.getSequenceAsString(new Properties());
            junit.framework.Assert.fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
        } catch (XQException e) {
        }
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        String result = null;
        try {
            result = xqs.getSequenceAsString(new Properties());
        } catch (XQException e) {
            junit.framework.Assert.fail("A-XQS-23.1: getSequenceAsString failed with message: " + e.getMessage());
        }
        junit.framework.Assert.assertTrue("A-XQS-23.1: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result + "'", result.indexOf("<e>Hello world!</e>") != -1);
        xqe.close();
        xqe = xqc.createExpression();
        xqs = xqe.executeQuery("<e>Hello world!</e>");
        try {
            junit.framework.Assert.assertEquals("A-XQS-23.2: null properties argument is equivalent to empty properties argument", result, xqs.getSequenceAsString(null));
        } catch (XQException e) {
            junit.framework.Assert.fail("A-XQS-23.2: getSequenceAsString failed with message: " + e.getMessage());
        }
        xqe.close();
    }
