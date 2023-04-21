    public void addReader(Link reader) {
        if ((!_readers.contains(reader)) && (!_writers.contains(reader)) && (!_managers.contains(reader))) {
            if (!LABEL_READER.equals(reader.targetLabel())) {
                reader = new Link(reader.targetName(), LABEL_READER, reader.targetAuthenticator());
            }
            super.add(reader);
            _readers.add(reader);
        }
    }
