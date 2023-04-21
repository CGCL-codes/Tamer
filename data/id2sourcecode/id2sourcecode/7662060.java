    public boolean stepThree() {
        Builder.logOutput.delete(0, Builder.logOutput.length());
        Builder.logOutput.append("\nPreparing source files...");
        Builder.logOutput.append("\nTemplate source: " + this.templateSrcPath);
        Builder.logOutput.append("\nOutput source: " + this.outputSrcFolder);
        for (final File parent : this.templateSrcPath.listFiles()) {
            if (parent.isDirectory()) {
                final File outP = new File(this.outputSrcFolder, parent.getName());
                outP.mkdir();
                Builder.logOutput.append("\nDirectory created: " + outP.getAbsolutePath());
                for (final File child : parent.listFiles()) {
                    if (child.getName().endsWith("java")) {
                        if (!this.copySourceFiles(child, outP, 1)) {
                            return false;
                        }
                    }
                }
            }
            if (parent.getName().endsWith("java")) {
                if (!this.copySourceFiles(parent, this.outputSrcFolder, 1)) {
                    return false;
                }
            }
        }
        return true;
    }
