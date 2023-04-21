    public static void main(String[] args) throws NumberFormatException {
        for (ModelBreaker breaker = new ModelBreaker(args[0], args[1], args[2], Integer.parseInt(args[3])); breaker.hasNextLine(); ) System.err.println(breaker.nextLine());
    }
