    public boolean doFullCheck(String rname) {
        int i = 0;
        try {
            Regatta baseline = RegattaManager.readRegattaFromDisk(BASEDIR, rname);
            assertNotNull("Unable to read: " + rname, baseline);
            Regatta tester = RegattaManager.readRegattaFromDisk(BASEDIR, rname);
            assertNotNull("Unable to read: " + rname, tester);
            String n = baseline.getName();
            i++;
            assertEquals(n + " init equals", tester, baseline);
            i++;
            assertEquals(n + " equals clone()", tester, tester.clone());
            i++;
            tester.scoreRegatta();
            readwriteCheck(tester);
            i++;
            readwriteCheck(baseline);
            return true;
        } catch (Exception e) {
            System.out.println();
            System.out.print("Exception in RegattaTests.doFullCheck( rname=");
            System.out.print(rname);
            System.out.println("), step=" + i);
            e.printStackTrace(System.out);
            return false;
        }
    }
