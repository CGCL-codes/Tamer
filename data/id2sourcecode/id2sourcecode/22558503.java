    public void perform() {
        if (initialized) {
            InputStream inputStream = initInput(operation.getInput());
            OutputStream outputStream = initOutput(operation.getOutput());
            try {
                int i;
                while ((i = inputStream.read()) != -1) {
                    digest.update((byte) i);
                }
                outputStream.write(digest.digest());
                inputStream.close();
                outputStream.close();
                if (operation.getOutput().equals("<Editor>")) {
                    EditorsManager.getInstance().openNewHexEditor(new PathEditorInput(URIUtil.toPath(getOutputURI())));
                }
            } catch (IOException e) {
                logger.error("IOException while performing a message digest", e);
            } catch (PartInitException e) {
                logger.error("PartInitException while performing a message digest", e);
            }
        }
    }
