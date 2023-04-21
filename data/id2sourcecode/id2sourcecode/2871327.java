    private String readFileContent(IFile file, RefactoringStatus status) {
        String result = null;
        try {
            InputStream is = file.getContents();
            byte buf[] = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int len = is.read(buf); len > 0; len = is.read(buf)) bos.write(buf, 0, len);
            is.close();
            result = new String(bos.toByteArray());
        } catch (Exception _ex) {
            String msg = Messages.Scope_FaildToReadFile.replaceFirst("\\[FileName\\]", file.getName());
            Log.write(net.sourceforge.refactor4pdt.log.Log.Level.FATAL_ERROR, getClass(), "readFileContent", msg);
            status.addFatalError(msg);
        }
        return result;
    }
