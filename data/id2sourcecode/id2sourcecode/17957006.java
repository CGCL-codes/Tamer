    private List<String> getTokenList(URL url) throws TokenisingException {
        URLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = url.openConnection();
            inputStream = urlConnection.getInputStream();
        } catch (FileNotFoundException e) {
            return new ArrayList<String>();
        } catch (IOException e) {
            throw new TokenisingException("cannot retrieve tokens", e);
        }
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            throw new TokenisingException("cannot retrieve tokens", e);
        }
        Pattern pattern = Pattern.compile("<a href=\"([^\"]*)/\">[^<]*</a>");
        Matcher m = pattern.matcher(buffer);
        List<String> result = new ArrayList<String>();
        while (m.find()) {
            String value = m.group(1);
            if (!"..".equals(value)) {
                result.add(value);
            }
        }
        return result;
    }
