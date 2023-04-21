    private static void debugWritingSize(Hessian2Output hos, FileOutputStream fos, String variableName, Long[] locations) {
        try {
            hos.flush();
            locations[0] = fos.getChannel().position();
            System.out.printf("%10d bytes : %s\n", (locations[0] - locations[1]), variableName);
            locations[1] = locations[0];
        } catch (IOException e) {
            return;
        }
    }
