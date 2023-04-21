    public void setAltitudeInMetres(ProcessContext context, RawOccurrenceRecord ror, OccurrenceRecord or) {
        String minAltitude = ror.getMinAltitude();
        String maxAltitude = ror.getMaxAltitude();
        String altitudePrecisionAsString = ror.getAltitudePrecision();
        Float minAltitudeAsFloat = null;
        Float maxAltitudeAsFloat = null;
        Integer gi = or.getGeospatialIssue();
        minAltitudeAsFloat = getAltitudeMeasurement(context, minAltitude, "min altitude", gi);
        maxAltitudeAsFloat = getAltitudeMeasurement(context, maxAltitude, "max altitude", gi);
        if (altitudePrecisionAsString != null) {
            altitudePrecisionAsString = removeMeasurementMarkers(altitudePrecisionAsString);
            Float altitudePrecision = getAltitudeMeasurement(context, altitudePrecisionAsString, "altitude precision", gi);
            try {
                if (altitudePrecision != null && minAltitudeAsFloat != null && maxAltitudeAsFloat != null) {
                    minAltitudeAsFloat -= altitudePrecision;
                    maxAltitudeAsFloat += altitudePrecision;
                }
            } catch (NumberFormatException e) {
                GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, "invalid or unparsable altitude precision");
                message.setCountOnly(true);
                logger.warn(message);
            }
        }
        or.setGeospatialIssue(gi);
        Integer altitudeAsInt = null;
        if (maxAltitudeAsFloat != null && minAltitudeAsFloat != null) {
            if ((maxAltitudeAsFloat + minAltitudeAsFloat) == 0) {
                altitudeAsInt = 0;
                or.setAltitudeInMetres(altitudeAsInt);
            } else if (maxAltitudeAsFloat == 0 && minAltitudeAsFloat > 0) {
                altitudeAsInt = Math.round(minAltitudeAsFloat);
                if (altitudeAsInt <= maxToRecordAltitudeInMetres && altitudeAsInt >= minToRecordAltitudeInMetres) {
                    or.setAltitudeInMetres(altitudeAsInt);
                }
            } else {
                Float altitude = (maxAltitudeAsFloat + minAltitudeAsFloat) / 2;
                altitudeAsInt = Math.round(altitude);
                if (altitudeAsInt <= maxToRecordAltitudeInMetres && altitudeAsInt >= minToRecordAltitudeInMetres) {
                    or.setAltitudeInMetres(altitudeAsInt);
                }
            }
        } else if (minAltitudeAsFloat != null) {
            altitudeAsInt = Math.round(minAltitudeAsFloat);
            if (altitudeAsInt <= maxToRecordAltitudeInMetres && altitudeAsInt >= minToRecordAltitudeInMetres) {
                or.setAltitudeInMetres(altitudeAsInt);
            }
        } else if (maxAltitudeAsFloat != null) {
            altitudeAsInt = Math.round(maxAltitudeAsFloat);
            if (altitudeAsInt <= maxToRecordAltitudeInMetres && altitudeAsInt >= minToRecordAltitudeInMetres) {
                or.setAltitudeInMetres(altitudeAsInt);
            }
        }
        if (altitudeAsInt != null && altitudeAsInt == 0) {
            GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, "number of records marker with altitude 0");
            message.setCountOnly(true);
            logger.warn(message);
        }
        if (altitudeAsInt != null && (altitudeAsInt > outOfRangeMaxAltitude || altitudeAsInt < outOfRangeMinAltitude)) {
            or.setGeospatialIssueBits(OccurrenceRecordUtils.GEOSPATIAL_ALTITUDE_OUT_OF_RANGE);
            GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, "number of records with out of range altitude (>" + outOfRangeMaxAltitude + " or <" + outOfRangeMinAltitude + ")");
            message.setCountOnly(true);
            logger.warn(message);
        }
        if (altitudeAsInt != null && (altitudeAsInt == -9999 || altitudeAsInt == 9999)) {
            or.setGeospatialIssueBits(OccurrenceRecordUtils.GEOSPATIAL_PRESUMED_ERRONOUS_ALTITUDE);
            GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, "number of records with altitude -9999");
            message.setCountOnly(true);
            logger.warn(message);
        }
        if (minAltitudeAsFloat != null && maxAltitudeAsFloat != null && minAltitudeAsFloat > maxAltitudeAsFloat && maxAltitudeAsFloat != 0) {
            or.setGeospatialIssueBits(OccurrenceRecordUtils.GEOSPATIAL_PRESUMED_MIN_MAX_ALTITUDE_REVERSED);
            GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, "number of records with min and max altitude transposed");
            message.setCountOnly(true);
            logger.warn(message);
        }
    }
