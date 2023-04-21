    public boolean stepSeven() {
        Builder.logOutput.delete(0, Builder.logOutput.length());
        if (this.iconPath.length() > 0) {
            final File icon = new File(this.iconPath);
            Builder.logOutput.append("\nCopying Icon...");
            for (final File f : new File(this.outputFolder, "res").listFiles()) {
                if (f.isDirectory() && f.getName().startsWith("drawable")) {
                    for (final File child : f.listFiles()) {
                        if (child.getName().equals("icon.png")) {
                            try {
                                child.delete();
                                this.copyFile(icon, child.getAbsoluteFile());
                                Builder.logOutput.append("\nCopied icon from: " + this.iconPath + " to: " + child.getAbsolutePath());
                            } catch (final IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                    }
                }
            }
        } else {
            Builder.logOutput.append("\nNo Icon set - nothing to do.");
        }
        return true;
    }
