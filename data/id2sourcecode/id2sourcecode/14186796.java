    @Test
    public void testReportObjectAccessMisc() throws Exception {
        AccessReporter<String> reporter2 = coordinator.registerAccessSource("s2", String.class);
        reporter.reportObjectAccess("read1", AccessType.READ);
        reporter2.reportObjectAccess("read1", AccessType.READ);
        reporter.reportObjectAccess("upgrade", AccessType.READ);
        reporter.reportObjectAccess("upgrade", AccessType.READ);
        reporter.reportObjectAccess("write1", AccessType.WRITE);
        reporter.reportObjectAccess("read1", AccessType.READ);
        reporter.reportObjectAccess("write1", AccessType.WRITE);
        reporter.reportObjectAccess("upgrade", AccessType.WRITE);
        reporter.reportObjectAccess("upgrade", AccessType.WRITE);
        txn.commit();
        txn = null;
        assertObjectDetails(profileCollector.getAccessedObjectsDetail(), "s", "read1", AccessType.READ, null, "s2", "read1", AccessType.READ, null, "s", "upgrade", AccessType.READ, null, "s", "write1", AccessType.WRITE, null, "s", "upgrade", AccessType.WRITE, null);
    }
