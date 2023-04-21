    public static void main(String args[]) {
        System.out.println("getKey test: reading HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Run");
        System.out.println(RegEdit.getKey("HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Run"));
        System.out.println("\ngetKey test: reading HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion");
        System.out.println(RegEdit.getKey("HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion"));
        System.out.println("\ngetValue test: trying to read DreamNode value.");
        System.out.println(RegEdit.getValue("HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "DreamNode"));
        System.out.println("\nwriteValue writing subkey \"DreamTest\", then reading it again (should output \"testvalue\"):");
        RegEdit.writeValue("HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "DreamTest", "testvalue");
        String getTestVal = RegEdit.getValue("HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "DreamTest");
        System.out.println(getTestVal);
        if (getTestVal.compareTo("testvalue") == 0) {
            System.out.println("RegEdit read/write test finished successfully.");
        } else {
            System.out.println("RegEdit read/write test failed.");
        }
        System.out.println("\nNow deleting the key again: (should output 'null')");
        RegEdit.deleteValue("HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "DreamTest");
        String getDeletedVal = RegEdit.getValue("HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "DreamTest");
        System.out.println(getDeletedVal);
        if (getDeletedVal == null) {
            System.out.println("RegEdit deletion test finished successfully.");
        } else {
            System.out.println("RegEdit deletion test failed.");
        }
        JFrame testMon = new JFrame();
        RegEdit.progressMonitorParent = testMon;
        System.out.println(lineSep + "testing progress monitoring.");
        RegEdit.progressMessage = "Reading from Windows registry.";
        RegEdit.getValue("HKEY_LOCAL_MACHINE\\SOFTWARE", "DreamTest");
        System.out.println("progress monitoring test finished.");
        RegEdit.progressMonitorParent = null;
        System.exit(0);
    }
