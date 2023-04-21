    public static void main(final String[] args) throws Exception {
        if (args == null || args.length == 0) throw createException("missing arguments");
        final ReportRdf reporter = new ReportRdf();
        String language = Extension.ttl.getLanguage();
        Logger.getLogger("").setLevel(Level.OFF);
        for (int i = 0; i < args.length; i++) {
            final Option option = toOption(args[i]);
            final String value = args[++i];
            switch(option) {
                case format:
                    language = toLanguage(value);
                    break;
                case gedcom:
                    reporter.convert(readGedcom(value)).write(System.out, language);
                    break;
                case rules:
                    reporter.queries.qRules = value;
                    break;
                case FAM:
                    reporter.uriFormats.fam = value;
                    break;
                case INDI:
                    reporter.uriFormats.indi = value;
                    break;
                case OBJE:
                    reporter.uriFormats.obje = value;
                    break;
                case NOTE:
                    reporter.uriFormats.note = value;
                    break;
                case REPO:
                    reporter.uriFormats.repo = value;
                    break;
                case SOUR:
                    reporter.uriFormats.sour = value;
                    break;
                case SUBM:
                    reporter.uriFormats.subm = value;
                    break;
                case uri:
                    reporter.uriFormats.subm = value;
                    reporter.uriFormats.fam = value;
                    reporter.uriFormats.indi = value;
                    reporter.uriFormats.obje = value;
                    reporter.uriFormats.note = value;
                    reporter.uriFormats.repo = value;
                    reporter.uriFormats.sour = value;
                    break;
            }
        }
    }
