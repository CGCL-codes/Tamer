    @Override
    @SuppressWarnings("unchecked")
    public QDataSet getDataSet(ProgressMonitor mon) throws Exception {
        logit("enter getDataSet", t0);
        if (inRequest) {
            logger.fine("came back again");
        } else {
            inRequest = true;
        }
        Map<String, String> params2 = new LinkedHashMap<String, String>(params);
        DatumFormatter df = new TimeDatumFormatter("%Y%m%d");
        int ppd;
        if (timeRange != null) {
            timeRange = quantizeTimeRange(timeRange);
            params2.put("StartDate", "" + df.format(timeRange.min()));
            params2.put("EndDate", "" + df.format(TimeUtil.prev(TimeUtil.DAY, timeRange.max())));
            params2.remove("timerange");
        } else {
            setTSBParameters();
        }
        if (currentPpd == -1) {
            params2.put("ppd", "1");
        } else {
            params2.put("ppd", "" + currentPpd);
        }
        mon.setTaskSize(-1);
        mon.started();
        if (!haveInitialTsml) {
            if (exceptionFromConstruct != null) {
                throw exceptionFromConstruct;
            }
            mon.setProgressMessage("loading parameter metadata");
            LinkedHashMap params3 = new LinkedHashMap(params2);
            params3.remove("ppd");
            params3.put("out", "tsml");
            URL url3 = new URL("" + this.resourceURI + "?" + URISplit.formatParams(params3));
            logger.log(Level.FINE, "opening {0}", url3);
            initialTsml(url3.openStream());
            haveInitialTsml = true;
            logit("got initial tsml", t0);
        }
        if (currentPpd == -1) {
            ppd = 1;
            params2.put("ppd", "" + ppd);
        } else {
            ppd = currentPpd;
        }
        URL url2 = new URL("" + this.resourceURI + "?" + URISplit.formatParams(params2));
        int points = (int) Math.ceil(timeRange.width().doubleValue(Units.days)) * ppd;
        int size = points * SIZE_DOUBLE;
        logit("making url2 connection", t0);
        logger.log(Level.FINE, "{0}", url2);
        HttpURLConnection connect = (HttpURLConnection) url2.openConnection();
        connect.connect();
        String type = connect.getContentType();
        logit("made url2 connection", t0);
        QDataSet result;
        if (params.get("out").equals("ncml")) {
            result = new TsmlNcml().doRead(url2, connect);
        } else if (type.startsWith("text/xml")) {
            result = tsml(connect.getInputStream(), mon);
            logit("done text/xml from url2", t0);
        } else {
            result = dataUrl(connect, size, points, -1, mon);
            logit("done dataUrl from url2", t0);
        }
        mon.finished();
        inRequest = false;
        return result;
    }
