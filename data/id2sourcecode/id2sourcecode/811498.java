    public void collectInto(List<String[]> results) {
        try {
            System.out.printf("Processando resource %d-%d \n", start, start + Ranking.PAGE_SIZE);
            HttpURLConnection urlConnection = openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(urlConnection)));
            results.addAll(parser.parse(reader));
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
