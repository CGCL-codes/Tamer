    public static void main(String argv[]) {
        TestCasts tc = new TestCasts();
        if (argv.length > 0) {
            int i;
            for (i = 0; i < argv.length; i++) {
                if (argv[i].equals("-g")) {
                    tc.generate = true;
                } else if (argv[i].equals("-v")) {
                    tc.verbose = true;
                } else if (argv[i].equals("-f")) {
                    i++;
                    if (i > argv.length) {
                        System.out.println("You need to specify filename after -f");
                        System.exit(1);
                    }
                    tc.filename = argv[i];
                } else {
                    System.out.println("Options are: -v -g -f file");
                    System.out.println("[-v] verbose ");
                    System.out.println("[-g] generate result table");
                    System.out.println("[-f file] read/write tests from/to file (default " + tc.filename + ")");
                    System.exit(1);
                }
            }
        }
        tc.test();
        System.out.println("Performed " + tc.counter + " tests");
        if (tc.generate) System.out.println("True: " + tc.genTrue + "\tfalse: " + tc.genFalse); else {
            System.out.println("Passed: " + tc.passed + "\tfailed: " + tc.failed);
            if (tc.failed == 0) System.out.println("PASSED: all cast tests");
        }
    }
