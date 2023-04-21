    public static junit.framework.Test suite() throws Exception {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(AllUnitTests.class.getName());
        suite.addTest(fedora.server.journal.readerwriter.multifile.AllUnitTests.suite());
        return suite;
    }
