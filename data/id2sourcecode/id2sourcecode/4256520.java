    public void addWriter(Link writer) {
        if ((!_writers.contains(writer)) && (!_managers.contains(writer))) {
            if (!LABEL_WRITER.equals(writer.targetLabel())) {
                writer = new Link(writer.targetName(), LABEL_WRITER, writer.targetAuthenticator());
            }
            if (_readers.contains(writer)) {
                removeReader(writer);
            }
            super.add(writer);
            _writers.add(writer);
        }
    }
