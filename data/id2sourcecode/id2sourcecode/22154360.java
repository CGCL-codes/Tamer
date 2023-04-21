    private static List<String> readLines(URL url) throws IOException {
        URLConnection uc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        List<String> result = new ArrayList<String>();
        String toAppend;
        while ((toAppend = in.readLine()) != null) {
            result.add(toAppend);
        }
        in.close();
        return result;
    }
