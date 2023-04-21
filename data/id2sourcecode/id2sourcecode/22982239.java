    private void showParametersExit() {
        System.out.println("Arguments: ");
        System.out.println("------------------------------------------");
        System.out.println("-help (shows this screen)");
        System.out.println("-i <input file>");
        System.out.println("-o <output file>");
        System.out.println("-if <input format> (1=one token per line, 2=one sentence per line, 3=some other different format ");
        System.out.println("-of <output format> (1=one token per line, 2=one sentence per line");
        System.out.println("-c <count> (optional; quit after <count> sentences)");
        System.out.println("-l <lexicon file> (optional)");
        System.out.println("-mwe (show multiword expressions; optional)");
        System.out.println("-sa (split abbreviations; optional)");
        System.out.println("-ns (not strict tokenization; optional)");
        System.out.println("------------------------------------------");
        System.out.println("If the parameters -i/-o are not provided then");
        System.out.println("the tokenizer reads from standard input and writes to standard output");
        System.out.println("------------------------------------------");
        System.exit(0);
    }
