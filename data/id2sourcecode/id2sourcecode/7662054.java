    public boolean stepFive() {
        Builder.logOutput.delete(0, Builder.logOutput.length());
        Builder.logOutput.append("\nDefining the Appname...");
        for (final File f : new File(this.outputFolder, "res").listFiles()) {
            if (f.isDirectory() && f.getName().startsWith("values")) {
                Builder.logOutput.append("\nLanguage: " + (f.getName().length() == 6 ? "en" : f.getName().substring(f.getName().length() - 2)));
                for (final File child : f.listFiles()) {
                    if (!child.isHidden()) {
                        if (!this.copySourceFiles(child, f, 2)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
