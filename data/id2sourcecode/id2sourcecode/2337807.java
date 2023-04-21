    public static final List<List<String>> getMetaDataByAccessions(final List<String> accessions) throws IOException {
        final List<List<String>> out = new ArrayList<List<String>>();
        final StringBuilder sb = new StringBuilder(__urlBase);
        for (int i = 0; i < accessions.size(); i++) {
            sb.append("accession:").append(accessions.get(i));
            if (i + 1 < accessions.size()) sb.append("+OR+");
        }
        HttpURLConnection connection = null;
        try {
            final URL url = new URL(sb.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(__connTimeout);
            connection.setReadTimeout(__readTimeout);
            connection.setRequestProperty("Connection", "close");
            connection.connect();
            final BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                final String[] strings = line.split("\\t");
                out.add(Arrays.asList(strings));
            }
        } finally {
            if (connection != null) connection.disconnect();
        }
        return out;
    }
