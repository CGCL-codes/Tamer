    public Map<String, Restaurant> processFile(String aURLString) throws MalformedURLException, IOException {
        LOGGER.info(format("Starting to process URL %s", aURLString));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Map<String, Restaurant> result = new HashMap<String, Restaurant>();
        final long startTime = System.currentTimeMillis();
        final URL url = new URL(aURLString);
        final URLConnection connection = url.openConnection();
        if (connection instanceof HttpURLConnection) {
            LOGGER.info("setting timeout to zero");
            ((HttpURLConnection) connection).setConnectTimeout(0);
            ((HttpURLConnection) connection).setReadTimeout(0);
        }
        final InputStream inputStream = connection.getInputStream();
        try {
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                LOGGER.info(format("processing zip entry %s", zipEntry.getName()));
                if (zipEntry.getName().equals("WebExtract.txt")) {
                    final BufferedReader br = new BufferedReader(new InputStreamReader(zipInputStream));
                    final String header = br.readLine();
                    if (!header.equals(EXPECTED_HEADER_FOR_WEB_EXTRACT)) {
                        throw new RuntimeException(format("Received unexpected header for WebExtract.txt: %s", header));
                    }
                    String line;
                    while ((line = br.readLine()) != null) {
                        try {
                            final Matcher matcher = PATTERN.matcher(line);
                            if (!matcher.matches()) {
                                throw new RuntimeException(format("Couldn't match WebExtract.txt record to pattern: %s", line));
                            }
                            final String camis = matcher.group(1);
                            final Date gradeDate;
                            final String gradeDateAtString = matcher.group(14);
                            if (gradeDateAtString.isEmpty()) {
                                gradeDate = null;
                            } else {
                                gradeDate = simpleDateFormat.parse(gradeDateAtString);
                            }
                            if (!result.containsKey(camis) || isLaterThan(gradeDate, result.get(camis).getGradeDate())) {
                                final String doingBusinessAs = matcher.group(2).trim();
                                final Borough borough = Borough.findByCode(Integer.parseInt(matcher.group(3)));
                                final String building = matcher.group(4).trim();
                                final String street = matcher.group(5).trim();
                                final String zipCode = matcher.group(6);
                                final String phoneNumber = matcher.group(7);
                                final String cuisine = matcher.group(8);
                                final Grade currentGrade = Grade.findByCode(matcher.group(13));
                                final Restaurant restaurant = new Restaurant(camis, doingBusinessAs, borough, new Address(building, street, zipCode), phoneNumber, cuisine, currentGrade, gradeDate);
                                result.put(camis, restaurant);
                            }
                        } catch (Exception e) {
                            LOGGER.log(WARNING, format("Unable to process record %s", line), e);
                        }
                    }
                }
            }
        } finally {
            inputStream.close();
        }
        final long endTime = System.currentTimeMillis();
        LOGGER.info(format("Finished processing URL %s in %,d ms", aURLString, (endTime - startTime)));
        return result;
    }
