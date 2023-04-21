    @Override
    public void run() {
        Object obj = Clipboard.getDefault().getContents();
        if (obj instanceof CopySnapshot) {
            CopySnapshot clipBoardCopy = (CopySnapshot) obj;
            execute(clipBoardCopy.getPasteCommand(getTargetDiagramModel(), fGraphicalViewer, fXMousePos, fYMousePos));
            setMouseClickPosition(-1, -1);
        }
    }
