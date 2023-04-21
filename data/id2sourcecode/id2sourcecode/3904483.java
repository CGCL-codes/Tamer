    public void shutDown() {
        try {
            this.toThread.write("</doc>".getBytes());
            toThread.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
