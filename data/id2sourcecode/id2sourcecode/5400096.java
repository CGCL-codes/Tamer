    private boolean readOutfileArgument() {
        String nl = Constants.LINE_SEPERATOR;
        if (!Commandline.get(this.arguments, "-out", true).equals("ERROR")) {
            this.outfile = Commandline.get(this.arguments, "-out", true).trim();
            if (!Commandline.get(this.arguments, "-f", false).equals("EXISTS")) {
                if (ReadFile.exists(this.outfile)) {
                    System.err.print(nl + "File \"" + outfile + "\" already exists. " + "Overwrite? [y/n]: ");
                    String respond = Commandline.get();
                    System.err.print(nl);
                    if (!respond.equalsIgnoreCase("y") && !respond.equalsIgnoreCase("yes")) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            return true;
        }
        return false;
    }
