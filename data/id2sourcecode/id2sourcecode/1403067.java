    public void loadAirports(final List<Airport> airports, final BufferedReader in, final IProgressMonitor monitor) throws IOException {
        StringTokenizer tokenizer;
        Airport airport = null;
        int airportCount = 0;
        long offset = 0;
        String buf = in.readLine();
        offset += buf.length() + 1;
        buf = in.readLine();
        offset += buf.length() + 1;
        buf = in.readLine();
        try {
            while (true) {
                Thread.yield();
                if (buf == null) {
                    break;
                }
                if (buf.length() > 0) {
                    tokenizer = new StringTokenizer(buf);
                    String rType = tokenizer.nextToken();
                    if (rType.equals("1") || rType.equals("16") || rType.equals("17")) {
                        final double elevation = new Double(tokenizer.nextToken()).doubleValue();
                        final boolean tower = tokenizer.nextToken().equals("1");
                        final boolean defaultBuildings = tokenizer.nextToken().equals("1");
                        final String id = tokenizer.nextToken();
                        final StringBuffer name = new StringBuffer();
                        while (tokenizer.hasMoreTokens()) {
                            name.append(tokenizer.nextToken());
                            name.append(" ");
                        }
                        _runwayOffsets.put(id, new Long(offset));
                        long maxLength = 0;
                        double latitude = 0;
                        double longitude = 0;
                        offset += buf.length() + 1;
                        buf = in.readLine();
                        while (true) {
                            if (buf == null) {
                                break;
                            }
                            if (buf.length() > 0) {
                                tokenizer = new StringTokenizer(buf);
                                rType = tokenizer.nextToken();
                                if (rType.equals("1") || rType.equals("16") || rType.equals("17")) {
                                    break;
                                }
                                if (rType.equals("100")) {
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    @SuppressWarnings("unused") final String r_number = tokenizer.nextToken();
                                    final double r_lat = new Double(tokenizer.nextToken()).doubleValue();
                                    final double r_long = new Double(tokenizer.nextToken()).doubleValue();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    @SuppressWarnings("unused") final String r1_number = tokenizer.nextToken();
                                    final double r1_lat = new Double(tokenizer.nextToken()).doubleValue();
                                    final double r1_long = new Double(tokenizer.nextToken()).doubleValue();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    Coordinate c1 = new Coordinate(r_lat, r_long);
                                    Coordinate c2 = new Coordinate(r1_lat, r1_long);
                                    final int r_length = (int) (c1.distanceTo(c2) * 6076);
                                    if (r_length > maxLength) {
                                        maxLength = r_length;
                                        latitude = (r_lat + r1_lat) / 2;
                                        longitude = (r_long + r1_long) / 2;
                                    }
                                }
                                if (rType.equals("101")) {
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    tokenizer.nextToken();
                                    latitude = new Double(tokenizer.nextToken()).doubleValue();
                                    longitude = new Double(tokenizer.nextToken()).doubleValue();
                                }
                                if (rType.equals("102")) {
                                    tokenizer.nextToken();
                                    latitude = new Double(tokenizer.nextToken()).doubleValue();
                                    longitude = new Double(tokenizer.nextToken()).doubleValue();
                                }
                            }
                            offset += buf.length() + 1;
                            buf = in.readLine();
                        }
                        airport = new Airport(id, latitude, longitude, elevation, "", tower, defaultBuildings, name.toString().trim(), maxLength);
                        airports.add(airport);
                        airportCount++;
                        if (airportCount % 1000 == 0) {
                            monitor.subTask(MessageFormat.format(Messages.getString("AirportParser.3"), airportCount));
                            monitor.worked(1000);
                        }
                        _airportMap.put(id, airport);
                    } else {
                        offset += buf.length() + 1;
                        buf = in.readLine();
                    }
                } else {
                    offset += buf.length() + 1;
                    buf = in.readLine();
                }
            }
        } catch (final EOFException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
