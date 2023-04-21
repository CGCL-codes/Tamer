    private void printProgramme(PrintStream out, TVProgramme prog) {
        out.print(dayTimeFormat.format(new Date(prog.getStart())));
        out.print(" ");
        out.print(prog.getTitle());
        if (prog.getSubTitle() != null) {
            out.print(": ");
            out.print(prog.getSubTitle());
        }
        out.print(" (");
        out.print(prog.getChannel().getDisplayName());
        out.println(")");
        out.flush();
    }
