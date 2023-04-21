    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(_nextNode);
        out.writeObject(_prevNode);
        out.writeObject(_parent);
        out.writeObject(_ownerDocument);
        if (_nodeName != null) {
            out.writeByte(1);
            out.writeUTF(_nodeName);
        } else {
            out.writeByte(-1);
        }
        if (_nodeValue != null) {
            out.writeByte(1);
            out.writeUTF(_nodeValue);
        } else {
            out.writeByte(-1);
        }
        out.writeObject(_firstChild);
        out.writeObject(_lastChild);
        out.writeInt(_childsCount);
        out.writeBoolean(_readOnly);
        if (_iterators != null) {
            int len = _iterators.length;
            out.writeInt(len);
            for (int i = 0; i < len; i++) {
                out.writeObject(_iterators[i]);
            }
        } else {
            out.writeInt(-1);
        }
    }
