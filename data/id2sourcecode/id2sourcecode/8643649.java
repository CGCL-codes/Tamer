		 * Create a transfer drop target listener. When using a CombinedTemplateCreationEntry
		 * tool in the palette, this will enable model element creation by dragging from the palette.
		 * @see #createPaletteViewerProvider()
		 */
    private TransferDropTargetListener createTransferDropTargetListener() {
        return new TemplateTransferDropTargetListener(getGraphicalViewer()) {

            protected CreationFactory getFactory(Object template) {
