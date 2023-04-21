    private static void printHelp() {
        System.out.println(" Usage is:");
        System.out.println("   java de.fraunhofer.ipsi.xquery.xqts.XQTSExecuter [options] XQTSROOT\n");
        System.out.println("     options :\n");
        System.out.println("        -output:outputfile     -  direct output to outputfile (default: XQTSResult.xml)");
        System.out.println("        -overwrite             -  overwrites an outputfile, if it already exists");
        System.out.println("        -include:testgroups    -  a comma separated list of top-level test-groups that should be executed");
        System.out.println("        -exclude:testgroups    -  a comma separated list of test-groups that should not be executed");
        System.out.println();
        System.out.println("     XQTSROOT   - the root folder of the XQTS installation (containing the XQTSCatalog.xml)");
        System.out.println();
        System.exit(0);
    }
