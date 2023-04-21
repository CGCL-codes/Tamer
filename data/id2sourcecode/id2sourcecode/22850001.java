    public void doIt(BufferedReader in, BufferedWriter out) throws Exception {
        out.write((Integer) ((Environment) Thread.currentThread()).getDs().pop());
    }
