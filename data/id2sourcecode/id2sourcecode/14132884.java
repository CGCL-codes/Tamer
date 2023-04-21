    private Float getDepthMeasurement(ProcessContext context, String depth, String fieldName, Integer geospatialIssues) {
        Float depthAsFloat = null;
        try {
            if (depth != null) {
                Matcher depthMatcher = measureMarkerPattern.matcher(depth);
                boolean containsNonnumeric = depthMatcher.matches();
                if (containsNonnumeric) {
                    geospatialIssues &= (OccurrenceRecordUtils.GEOSPATIAL_MASK ^ OccurrenceRecordUtils.GEOSPATIAL_PRESUMED_DEPTH_NON_NUMERIC);
                    boolean isInFeet = feetMarkerPattern.matcher(depth).matches();
                    boolean isInInches = inchesMarkerPattern.matcher(depth).matches();
                    GbifLogMessage nonNumericMessage = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, fieldName + " contains non-numeric characters");
                    nonNumericMessage.setCountOnly(true);
                    logger.warn(nonNumericMessage);
                    if (sepMarkerPattern.matcher(depth).matches()) {
                        try {
                            String min = depth.substring(0, depth.indexOf('-')).trim();
                            min = removeMeasurementMarkers(min);
                            String max = depth.substring(depth.indexOf('-') + 1).trim();
                            max = removeMeasurementMarkers(max);
                            GbifLogMessage rangeMessage = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, fieldName + " contains range supplied in single field");
                            rangeMessage.setCountOnly(true);
                            logger.warn(rangeMessage);
                            Float minFloat = Float.parseFloat(min);
                            Float maxFloat = Float.parseFloat(max);
                            if (minFloat != 0 && maxFloat != 0 && (maxFloat - minFloat) != 0) {
                                depthAsFloat = (maxFloat + minFloat) / 2;
                            }
                        } catch (NumberFormatException e) {
                        }
                    } else {
                        depth = removeMeasurementMarkers(depth);
                        depthAsFloat = Float.parseFloat(depth);
                    }
                    if (depthAsFloat != null) {
                        if (isInFeet || isInInches) {
                            geospatialIssues &= (OccurrenceRecordUtils.GEOSPATIAL_MASK ^ OccurrenceRecordUtils.GEOSPATIAL_PRESUMED_DEPTH_IN_FEET);
                        }
                        if (isInFeet) {
                            depthAsFloat = convertFeetToMetres(depthAsFloat);
                        } else if (isInInches) {
                            depthAsFloat = convertInchesToMetres(depthAsFloat);
                        }
                    }
                } else {
                    depthAsFloat = Float.parseFloat(depth);
                }
            }
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        return depthAsFloat;
    }
