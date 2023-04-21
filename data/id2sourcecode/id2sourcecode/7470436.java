    public void saveChanges(String i_newContent) throws WriteToTargetFileFailure {
        this._isWriting = true;
        OutputStreamWithWriteLine outputStream = null;
        this._linesTransferred = 0;
        this.notifyObserversUpdateStatusLine();
        try {
            outputStream = this._editedFile.getOutputStream(true);
            java.io.StringReader stringReader = new java.io.StringReader(i_newContent);
            java.io.LineNumberReader lineNumberReader = new java.io.LineNumberReader(stringReader);
            while (true) {
                String nextLine = lineNumberReader.readLine();
                if (nextLine == null) {
                    break;
                }
                outputStream.writeLine(nextLine);
                this._linesTransferred++;
                this.notifyObserversUpdateStatusLine();
            }
            outputStream.close();
            this._isWriting = false;
        } catch (Exception e) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (java.io.IOException ex) {
                }
            }
            this._isWriting = false;
            if (e instanceof ftraq.fs.exceptions.WriteToTargetFileFailure) {
                throw (ftraq.fs.exceptions.WriteToTargetFileFailure) e;
            }
            throw new ftraq.fs.exceptions.WriteToTargetFileFailure(e, this._editedFile);
        }
    }
