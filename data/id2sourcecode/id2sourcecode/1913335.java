    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        boolean fromurl;
        int offset;
        String href;
        URL url;
        Cursor cursor;
        fromurl = in.readBoolean();
        if (fromurl) {
            offset = in.readInt();
            href = (String) in.readObject();
            in.defaultReadObject();
            if (null != getUrl()) {
                url = new URL(getUrl());
                try {
                    setConnection(url.openConnection());
                } catch (ParserException pe) {
                    throw new IOException(pe.getMessage());
                }
            }
            cursor = new Cursor(this, 0);
            for (int i = 0; i < offset; i++) try {
                getCharacter(cursor);
            } catch (ParserException pe) {
                throw new IOException(pe.getMessage());
            }
            setUrl(href);
        } else {
            href = (String) in.readObject();
            in.defaultReadObject();
            setUrl(href);
        }
    }
