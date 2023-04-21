    private void loadRunwaysForAirport(final Airport airport, final BufferedReader rdrRunwayIls, final BufferedReader rdrAirport) throws IOException {
        StringTokenizer tokenizer;
        String buf;
        boolean match = true;
        buf = rdrAirport.readLine();
        while (match) {
            Thread.yield();
            if (buf == null) {
                break;
            }
            if (buf.length() > 0) {
                tokenizer = new StringTokenizer(buf);
                final String rType = tokenizer.nextToken();
                if (rType.equals("100")) {
                    final int r_width = (int) (new Double(tokenizer.nextToken()).doubleValue() * 3.28);
                    final String r_surface = tokenizer.nextToken();
                    tokenizer.nextToken();
                    tokenizer.nextToken();
                    tokenizer.nextToken();
                    final String r_edgeLights = tokenizer.nextToken();
                    tokenizer.nextToken();
                    final String r_number = tokenizer.nextToken();
                    final double r_lat = new Double(tokenizer.nextToken()).doubleValue();
                    final double r_long = new Double(tokenizer.nextToken()).doubleValue();
                    tokenizer.nextToken();
                    tokenizer.nextToken();
                    final String r_markings = tokenizer.nextToken();
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
                    final double r_hdg = Math.toDegrees(c1.bearingTo(c2));
                    final double latitude = (r_lat + r1_lat) / 2;
                    final double longitude = (r_long + r1_long) / 2;
                    final Runway runway = new Runway(r_number, latitude, longitude, r_length, r_width, r_hdg, false, r_surface, r_edgeLights, r_markings);
                    airport.addRunway(runway);
                    _runwayMap.put(airport.getId() + runway.getNumber(), runway);
                    _runwayOppositeMap.put(airport.getId() + runway.getOppositeNumber(), runway);
                    buf = rdrAirport.readLine();
                } else if (rType.equals("110")) {
                    List<LayoutNode> nodes = new ArrayList<LayoutNode>();
                    while (true) {
                        buf = rdrAirport.readLine();
                        if (buf == null) {
                            break;
                        }
                        if (buf.length() > 0) {
                            tokenizer = new StringTokenizer(buf);
                            final String nType = tokenizer.nextToken();
                            if (nType.equals("111") || nType.equals("113") || nType.equals("115")) {
                                final double r1_lat = new Double(tokenizer.nextToken()).doubleValue();
                                final double r1_long = new Double(tokenizer.nextToken()).doubleValue();
                                nodes.add(new LayoutNode(nType, r1_lat, r1_long));
                            } else if (nType.equals("112") || nType.equals("114") || nType.equals("116")) {
                                final double r1_lat = new Double(tokenizer.nextToken()).doubleValue();
                                final double r1_long = new Double(tokenizer.nextToken()).doubleValue();
                                final double b1_lat = new Double(tokenizer.nextToken()).doubleValue();
                                final double b1_long = new Double(tokenizer.nextToken()).doubleValue();
                                nodes.add(new LayoutNode(nType, r1_lat, r1_long, b1_lat, b1_long));
                            } else {
                                break;
                            }
                        }
                    }
                    Taxiway taxiway = new Taxiway(null, nodes, false, "", "");
                    airport.addTaxiway(taxiway);
                } else if (rType.equals("1") || rType.equals("16") || rType.equals("17")) {
                    match = false;
                } else {
                    buf = rdrAirport.readLine();
                }
            } else {
                buf = rdrAirport.readLine();
            }
        }
        loadIls(rdrRunwayIls, airport);
    }
