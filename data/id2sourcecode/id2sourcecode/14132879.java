    public void setDepthInCentimetres(ProcessContext context, RawOccurrenceRecord ror, OccurrenceRecord or) {
        Float depth = null;
        Integer gi = or.getGeospatialIssue();
        Float precision = getDepthMeasurement(context, ror.getDepthPrecision(), "depth precision", gi);
        Float min = getDepthMeasurement(context, ror.getMinDepth(), "min depth", gi);
        Float max = getDepthMeasurement(context, ror.getMaxDepth(), "max depth", gi);
        if (min != null || max != null) {
            if (min == null) min = max; else if (max == null) max = min;
            if (precision != null) {
                min -= precision;
                max += precision;
            }
            if (max + min != 0) {
                depth = (max + min) / 2;
            } else {
                depth = 0f;
            }
            if (depth != null) {
                int depthInCentimetres = Math.round(depth * 100);
                if (depthInCentimetres <= maxRecordDepthInCentimetres && depthInCentimetres >= minRecordDepthInCentimetres) {
                    or.setDepthInCentimetres(Math.round(depth * 100));
                }
            }
            if ((min != null || max != null) && min > max) {
                or.setGeospatialIssueBits(OccurrenceRecordUtils.GEOSPATIAL_PRESUMED_MIN_MAX_DEPTH_REVERSED);
                GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, "min and max depth transposed");
                message.setCountOnly(true);
                logger.warn(message);
            }
            if (depth != null && depth > outOfRangeDepth) {
                or.setGeospatialIssueBits(OccurrenceRecordUtils.GEOSPATIAL_DEPTH_OUT_OF_RANGE);
                GbifLogMessage message = gbifLogUtils.createGbifLogMessage(context, LogEvent.EXTRACT_GEOSPATIALISSUE, "number of records with out of range depth (>" + outOfRangeDepth + ")");
                message.setCountOnly(true);
                logger.warn(message);
            }
        }
    }
