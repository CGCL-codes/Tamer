    public String getSequenceAsString(Properties props) throws XQException {
        checkNotClosed();
        StringBuffer sb = new StringBuffer();
        if (this.retrieved) {
            throw new XQException("Forward only sequence, a get or write method has already been invoked on the current item");
        }
        if (this.position == 0) next();
        sb.append(getCurrentXQItem(true).getItemAsString(props));
        while (next()) {
            sb.append(" " + getCurrentXQItem(true).getItemAsString(props));
        }
        return sb.toString();
    }
