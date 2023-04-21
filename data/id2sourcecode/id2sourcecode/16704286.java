    public void setText(Blob text) {
        this.text = text;
        try {
            InputStreamReader istr = new InputStreamReader(text.getBinaryStream());
            StringWriter sw = new StringWriter();
            while (istr.ready()) {
                sw.write(istr.read());
            }
            this.textString = sw.getBuffer().toString();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Invalid stream!", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Caught IOException!", e);
        }
    }
