    private Float getAltitudeMeasurement(ProcessContext context, String altitude, String fieldName, Integer geospatialIssues) {
        Float altitudeAsFloat = null;
        try {
            if (altitude != null) {
                Matcher altitudeMatcher = measureMarkerPattern.matcher(altitude);
                boolean containsNonnumeric = altitudeMatcher.matches();
                if (containsNonnumeric) {
                    geospatialIssues &= (OccurrenceRecordUtils.GEOSPATIAL_MASK ^ OccurrenceRecordUtils.GEOSPATIAL_PRESUMED_ALTITUDE_NON_NUMERIC);
                    boolean isInFeet = feetMarkerPattern.matcher(altitude).matches();
                    boolean isInInches = inchesMarkerPattern.matcher(altitude).matches();
                    GbifLogMessage nonNumericMessage = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, fieldName + " contains non-numeric characters");
                    nonNumericMessage.setCountOnly(true);
                    logger.warn(nonNumericMessage);
                    if (sepMarkerPattern.matcher(altitude).matches()) {
                        try {
                            String min = altitude.substring(0, altitude.indexOf('-')).trim();
                            min = removeMeasurementMarkers(min);
                            String max = altitude.substring(altitude.indexOf('-') + 1).trim();
                            max = removeMeasurementMarkers(max);
                            GbifLogMessage rangeMessage = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, fieldName + " contains range supplied in single field");
                            rangeMessage.setCountOnly(true);
                            logger.warn(rangeMessage);
                            Float minFloat = Float.parseFloat(min);
                            Float maxFloat = Float.parseFloat(max);
                            if (minFloat != 0 && maxFloat != 0 && (maxFloat - minFloat) != 0) {
                                altitudeAsFloat = (maxFloat + minFloat) / 2;
                            }
                        } catch (NumberFormatException e) {
                        }
                    } else {
                        altitude = removeMeasurementMarkers(altitude);
                        altitudeAsFloat = Float.parseFloat(altitude);
                    }
                    if (altitudeAsFloat != null) {
                        if (isInFeet || isInInches) {
                            geospatialIssues &= (OccurrenceRecordUtils.GEOSPATIAL_MASK ^ OccurrenceRecordUtils.GEOSPATIAL_PRESUMED_ALTITUDE_IN_FEET);
                        }
                        if (isInFeet) {
                            altitudeAsFloat = convertFeetToMetres(altitudeAsFloat);
                        } else if (isInInches) {
                            altitudeAsFloat = convertInchesToMetres(altitudeAsFloat);
                        }
                    }
                } else {
                    altitudeAsFloat = Float.parseFloat(altitude);
                }
            }
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        return altitudeAsFloat;
    }
