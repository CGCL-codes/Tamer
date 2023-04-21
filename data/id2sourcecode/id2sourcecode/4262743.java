    private void ignoreStreamCorruption(Exception ex) {
        String message = "Stream corruption found while reading a transaction from the journal. If this is a transaction that was being written when a system crash occurred, there is no problem because it was never executed on the Prevalent System. Before executing each transaction, Prevayler writes it to the journal and calls the java.io.FileDescritor.sync() method to instruct the Java API to physically sync all operating system RAM buffers to disk.";
        _monitor.notify(this.getClass(), message, _file, ex);
    }
