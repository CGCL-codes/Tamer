    public static void main(String[] args) {
        String line = readLn();
        while (line != null) {
            int i = process(line);
            System.out.println("The string '" + line + "' contains " + i + " palindromes.");
            line = readLn();
        }
    }
