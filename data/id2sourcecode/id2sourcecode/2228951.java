    public void run() {
        if (query == null) {
            System.err.println("Error! The setQuery-Method should be invoked before the run-Method!");
        } else {
            Object result = answerquery(query);
            pool.writeData(threadID, result);
        }
    }
