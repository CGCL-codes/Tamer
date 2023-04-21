    public void openFile(URL url) {
        try {
            this.matrix = MatrixFactory.getMatrix(ftype);
            this.matrix.read(url.openStream());
            this.setMatrix(matrix, url.toString());
        } catch (IOException ex) {
            Logger.getLogger(MatrixViewerDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
