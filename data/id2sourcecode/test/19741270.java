    static void usage(String m) {
        System.out.println("This is the Learner Quality Test Application for the BOXER toolkit (version " + Version.version + ")");
        System.out.println("Usage: java [options] edu.dimacs.applications.learning.Driver command:file [command:file] ...");
        System.out.println("For example:");
        System.out.println("  java [options] edu.dimacs.applications.learning.Driver [train:]train.xml [test:]test.xml");
        System.out.println("  java [options] edu.dimacs.applications.learning.Driver [read-suite:suite-in.xml] train:train1.xml train:train2.xml test:test_a.xml train:train3.xml test:test_b.xml [write:model-out.xml]");
        System.out.println(" ... etc.");
        System.out.println("Optons:");
        System.out.println(" [-Dmodel=eg|tg|trivial] [-Drunid=RUN_ID] [-Dverbose=true|false | -Dverbosity=0|1|2|3] [-DM=1] [-Drandom=100]");
        System.out.println("  -Drandom=nn : repeat the experiment nn times, with different orderings");
        System.out.println("See Javadoc for edu.dimacs.applications.learning.Driver for the full list of commands.");
        if (m != null) {
            System.out.println(m);
        }
        System.exit(1);
    }
