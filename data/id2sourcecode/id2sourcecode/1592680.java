    public void run() {
        if (query == null) {
            System.err.println("Error! The setQuery-Method should be invoked before the run-Method!");
        } else {
            Result result = load(query);
            System.out.println(result);
            pool.writeData(threadID, result);
        }
    }
