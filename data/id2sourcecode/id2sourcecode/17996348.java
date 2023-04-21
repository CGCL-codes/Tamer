    public static junit.framework.Test suite() throws Exception {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(AllUnitTests.class.getName());
        suite.addTest(org.fcrepo.server.journal.helpers.AllUnitTests.suite());
        suite.addTest(org.fcrepo.server.journal.readerwriter.AllUnitTests.suite());
        suite.addTest(org.fcrepo.server.journal.xmlhelpers.AllUnitTests.suite());
        suite.addTest(TestJournalRoundTrip.suite());
        return suite;
    }
