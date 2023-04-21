    public static junit.framework.Test suite() throws Exception {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(AllUnitTests.class.getName());
        suite.addTest(fedora.server.journal.helpers.AllUnitTests.suite());
        suite.addTest(fedora.server.journal.readerwriter.AllUnitTests.suite());
        suite.addTest(fedora.server.journal.xmlhelpers.AllUnitTests.suite());
        return suite;
    }
