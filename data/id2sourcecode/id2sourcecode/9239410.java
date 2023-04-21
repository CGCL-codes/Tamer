    protected void onCanNotReadFile(String filename) throws IOControllerException {
        new DebugWriter().writeMessage("Can not read file '" + filename + "'", DebugWriter.VERBOSE_ANYTHING);
        throwCancelException();
    }
