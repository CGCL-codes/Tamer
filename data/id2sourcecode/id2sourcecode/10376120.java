    public void read_apt_table() throws Exception {
        logger.fine("Reading NAV database ( " + this.pathname_to_aptnav + this.APT_file + " )");
        File file = new File(this.pathname_to_aptnav + this.APT_file);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        long line_number = 0;
        int info_type;
        String airport_icao_code = "";
        String airport_name = "";
        boolean current_airport_saved = true;
        ArrayList runways = new ArrayList();
        float width;
        int surface;
        String rwy_num1;
        float thr1_lat;
        float thr1_lon;
        String rwy_num2;
        float thr2_lat;
        float thr2_lon;
        float length;
        float lat = 0;
        float lon = 0;
        float arp_lat = 0;
        float arp_lon = 0;
        float longest = 0;
        float rwy_count = 0;
        float lat_sum = 0;
        float lon_sum = 0;
        float hard_rwy_count = 0;
        float hard_lat_sum = 0;
        float hard_lon_sum = 0;
        boolean tower = false;
        while ((line = reader.readLine()) != null) {
            if (line.length() > 0) {
                line_number++;
                line = line.trim();
                if ((line_number > 2) && (!line.equals("99"))) {
                    try {
                        info_type = Integer.parseInt(line.substring(0, 3).trim());
                        if (info_type == 1) {
                            if (!current_airport_saved) {
                                if (hard_rwy_count == 1) {
                                    arp_lat = hard_lat_sum;
                                    arp_lon = hard_lon_sum;
                                } else if (rwy_count == 1) {
                                    arp_lat = lat_sum;
                                    arp_lon = lon_sum;
                                } else if (tower) {
                                    arp_lat = lat;
                                    arp_lon = lon;
                                } else if (hard_rwy_count > 1) {
                                    arp_lat = hard_lat_sum / hard_rwy_count;
                                    arp_lon = hard_lon_sum / hard_rwy_count;
                                } else {
                                    arp_lat = lat_sum / rwy_count;
                                    arp_lon = lon_sum / rwy_count;
                                }
                                nor.add_nav_object(new Airport(airport_name, airport_icao_code, arp_lat, arp_lon, runways, longest));
                            }
                            airport_icao_code = line.substring(15, 19);
                            airport_name = line.substring(20);
                            current_airport_saved = false;
                            runways = new ArrayList();
                            arp_lat = 0;
                            arp_lon = 0;
                            longest = 0;
                            rwy_count = 0;
                            lat_sum = 0;
                            lon_sum = 0;
                            hard_rwy_count = 0;
                            hard_lat_sum = 0;
                            hard_lon_sum = 0;
                            tower = false;
                        } else if (info_type == 100) {
                            width = Float.parseFloat(line.substring(5, 11));
                            surface = Integer.parseInt(line.substring(13, 15).trim());
                            rwy_num1 = line.substring(31, 34);
                            thr1_lat = Float.parseFloat(line.substring(35, 47));
                            thr1_lon = Float.parseFloat(line.substring(48, 61));
                            rwy_num2 = line.substring(87, 90);
                            thr2_lat = Float.parseFloat(line.substring(91, 103));
                            thr2_lon = Float.parseFloat(line.substring(104, 117));
                            length = CoordinateSystem.rough_distance(thr1_lat, thr1_lon, thr2_lat, thr2_lon) * 1851.852f;
                            lat = (thr1_lat + thr2_lat) / 2;
                            lon = (thr1_lon + thr2_lon) / 2;
                            if (XHSIPreferences.get_instance().get_draw_runways()) {
                                nor.add_nav_object(new Runway(airport_icao_code, length, width, surface, rwy_num1, thr1_lat, thr1_lon, rwy_num2, thr2_lat, thr2_lon));
                                runways.add(nor.get_runway(airport_icao_code, lat, lon));
                            }
                            if (length > longest) longest = length;
                            rwy_count += 1;
                            lat_sum += lat;
                            lon_sum += lon;
                            if ((surface == Runway.RWY_ASPHALT) || (surface == Runway.RWY_CONCRETE)) {
                                hard_rwy_count += 1;
                                hard_lat_sum += lat;
                                hard_lon_sum += lon;
                            }
                        } else if (info_type == 14) {
                            tower = true;
                            lat = Float.parseFloat(line.substring(4, 16));
                            lon = Float.parseFloat(line.substring(17, 30));
                        } else if ((info_type == 99) && (current_airport_saved == false)) {
                            if (!current_airport_saved) nor.add_nav_object(new Airport(airport_name, airport_icao_code, lat, lon, runways, longest));
                            current_airport_saved = true;
                        }
                    } catch (Exception e) {
                        logger.warning("\nParse error in " + file.getName() + ":" + line_number + "(" + e + ") " + line);
                    }
                }
            }
        }
        if (reader != null) {
            reader.close();
        }
    }
