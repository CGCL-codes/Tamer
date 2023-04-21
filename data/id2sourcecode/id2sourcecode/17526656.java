    public void help() {
        System.out.println("Comparator tool: reads 2 JSON and or XML texts and writes to STDOUT comparison results.");
        System.out.println(" diff [-format=JSON|XML|Dump] [-indent=true|false] [-locale=locale] [-depth=number] [-delta=true|false] [-help] <1st input file or URL> <2nd input file or URL>");
        System.out.println("   -format - defines output format. Default is JSON.");
        System.out.println("   -indent - sets/clears output indentation. Default is true.");
        System.out.println("   -locale - Name of locale to use when parsing input.");
        System.out.println("   -depth  - Maximal nesint depth when comparing. Default is 100.");
        System.out.println("   -delta  - Shows only mismatching nodes info if true. Default is false.");
        System.out.println("   1st file  - Path or URL for first compared file.  In oputput it corresponds to A.");
        System.out.println("   2nd file  - Path or URL for second compared file. In oputput it corresponds to B.");
    }
