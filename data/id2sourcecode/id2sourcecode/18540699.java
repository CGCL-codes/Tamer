    public Template(Reader reader, boolean writeSpaces) throws IOException {
        Checker.checkNull(reader, "reader");
        _lines = new Text(reader).getLines();
        _writeSpaces = writeSpaces;
        _currentLine = 0;
    }
