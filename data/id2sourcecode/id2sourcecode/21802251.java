    public TsdsDataSource(URI uri) {
        super(uri);
        try {
            addCability(TimeSeriesBrowse.class, getTimeSeriesBrowse());
            setTSBParameters();
            ProgressMonitor mon = new NullProgressMonitor();
            URL url0 = new URL("" + this.resourceURI + "?" + URISplit.formatParams(params));
            logger.log(Level.FINE, "tsds url= {0}", url0);
            if (params.get("out") == null) {
                exceptionFromConstruct = new IllegalArgumentException("url must contain out=");
                return;
            }
            mon.setProgressMessage("loading parameter metadata");
            LinkedHashMap<String, String> params3 = new LinkedHashMap<String, String>(params);
            params3.put("out", "tsml");
            params3.remove("ppd");
            String sparams = URISplit.formatParams(params3);
            sparams = sparams.replace("out=tsml", "out=tsml&ext=" + params.get("out"));
            logit("post first request in construct TsdsDataSource", t0);
            URL url3 = new URL("" + this.resourceURI + "?" + sparams);
            logger.log(Level.FINE, "opening {0}", url3);
            initialTsml(url3.openStream());
            if (hasEndDate == false) {
                params.put("EndDate", TimeParser.create("$Y$m$d").format(TimeUtil.prevMidnight(timeRange.max().subtract(Units.days.createDatum(1))), null));
            }
            logit("read initial tsml", t0);
            haveInitialTsml = true;
            setTSBParameters();
            parameterPpd = currentPpd;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TsdsDataSource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TsdsDataSource.class.getName()).log(Level.SEVERE, null, ex);
            exceptionFromConstruct = ex;
        } catch (SAXException ex) {
            Logger.getLogger(TsdsDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
