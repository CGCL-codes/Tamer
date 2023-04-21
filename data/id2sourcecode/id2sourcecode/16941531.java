    public void compile(CompilerContext context) throws CompileException {
        try {
            Lolcode compiler = new Lolcode();
            StringWriter writer = new StringWriter();
            InputStream input = context.getFile().getContents();
            while (input.available() > 0) {
                writer.write(input.read());
            }
            if (compiler.compile(writer.toString())) {
                File file = context.getFile().getRawLocation().removeFileExtension().addFileExtension(COMPILED_EXTENSION).toFile();
                if (!file.exists()) {
                    file.createNewFile();
                }
                compiler.serialize(file.getCanonicalPath());
                context.getFile().getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
                IFile compiledFile = context.getFile().getProject().getFile(context.getFile().getProjectRelativePath().removeFileExtension().addFileExtension(COMPILED_EXTENSION));
                if (compiledFile.exists()) {
                    compiledFile.setDerived(true);
                }
            } else {
                context.addError(compiler.getErrorLine(), 0, "Problem compiling this line");
            }
        } catch (Exception e) {
            throw new CompileException(e);
        }
    }
