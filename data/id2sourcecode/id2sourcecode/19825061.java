    @Override
    public synchronized QDataSet getDataSet(final ProgressMonitor mon) throws Exception {
        mon.started();
        mon.setProgressMessage("sending request");
        Map<String, String> params2 = new LinkedHashMap();
        Map<String, String> otherParams = new LinkedHashMap(params);
        otherParams.remove("start_time");
        otherParams.remove("end_time");
        otherParams.remove("resolution");
        otherParams.remove("dataset");
        otherParams.remove("tsb");
        String item = (String) otherParams.remove("item");
        String interval = (String) otherParams.remove("interval");
        String key1 = (String) otherParams.remove("key");
        dsParams = (String) URISplit.formatParams(otherParams);
        params2.put("server", "dataset");
        if (timeRange != null) {
            params2.put("start_time", URLEncoder.encode(timeRange.min().toString(), "US-ASCII"));
            params2.put("end_time", URLEncoder.encode(timeRange.max().toString(), "US-ASCII"));
        } else {
            throw new IllegalArgumentException("timeRange is null");
        }
        if (resolution != null) {
            params2.put("resolution", "" + resolution.doubleValue(Units.seconds));
        }
        String dataset = params.get("dataset");
        if (dataset == null) {
            dataset = params.get("arg_0");
        }
        if (interval != null) {
            params2.put("interval", URLEncoder.encode(interval, "US-ASCII"));
            params2.remove("resolution");
        }
        params2.put("dataset", URLEncoder.encode(dataset, "US-ASCII"));
        if (dsParams.length() > 0) {
            params2.put("params", dsParams);
        }
        URL url2 = new URL("" + this.resourceURI + "?" + URISplit.formatParams(params2));
        if (true) {
            if (dsdfParams == null) {
                String dsdfURL = this.resourceURI + "?server=dsdf&dataset=" + dataset;
                URL url3 = new URL(dsdfURL);
                logger.log(Level.FINE, "opening {0}", url3);
                InputStream in = url3.openStream();
                ReadableByteChannel channel = Channels.newChannel(in);
                final Map map = new LinkedHashMap();
                DataSetStreamHandler handler = new DataSetStreamHandler(new HashMap(), mon) {

                    @Override
                    public void streamDescriptor(StreamDescriptor sd) throws StreamException {
                        super.streamDescriptor(sd);
                        map.putAll(sd.getProperties());
                    }
                };
                StreamTool.readStream(channel, handler);
                channel.close();
                dsdfParams = map;
            }
            tcaDesc = new ArrayList<String>();
            int iplane = 0;
            String label = (String) dsdfParams.get("label");
            while (label != null) {
                tcaDesc.add(label);
                iplane++;
                label = (String) dsdfParams.get("plane_" + iplane + ".label");
            }
            String groupAccess = (String) dsdfParams.get("groupAccess");
            if (groupAccess != null && groupAccess.trim().length() > 0) {
                if (key1 == null) {
                    Authenticator authenticator;
                    authenticator = new Authenticator(DasServer.create(this.resourceURI.toURL()), groupAccess);
                    Key key2 = authenticator.authenticate();
                    if (key2 != null) {
                        params2.put("key", key2.toString());
                        url2 = new URL("" + this.resourceURI + "?" + URISplit.formatParams(params2));
                    }
                } else {
                    params2.put("key", key1);
                    url2 = new URL("" + this.resourceURI + "?" + URISplit.formatParams(params2));
                }
            }
        }
        logger.fine(String.valueOf(url2));
        boolean qds = "1".equals(dsdfParams.get("qstream"));
        logger.log(Level.FINE, "opening {0} {1}", new Object[] { qds ? "as qstream" : "as das2stream", url2 });
        InputStream in = url2.openStream();
        final DasProgressMonitorInputStream mpin = new DasProgressMonitorInputStream(in, mon);
        ReadableByteChannel channel = Channels.newChannel(mpin);
        QDataSet result1;
        if (qds) {
            try {
                org.virbo.qstream.QDataSetStreamHandler eh = new org.virbo.qstream.QDataSetStreamHandler();
                org.virbo.qstream.StreamTool.readStream(channel, eh);
                result1 = eh.getDataSet();
            } catch (org.virbo.qstream.StreamException ex) {
                if (ex.getCause() != null && (ex.getCause() instanceof java.io.InterruptedIOException)) {
                    ex.printStackTrace();
                    throw (java.io.InterruptedIOException) ex.getCause();
                } else {
                    ex.printStackTrace();
                    throw ex;
                }
            }
        } else {
            DataSetStreamHandler handler = new DataSetStreamHandler(new HashMap(), mon) {

                @Override
                public void streamDescriptor(StreamDescriptor sd) throws StreamException {
                    super.streamDescriptor(sd);
                    if (mon.getTaskSize() != -1) {
                        mpin.setEnableProgressPosition(false);
                    }
                }
            };
            try {
                StreamTool.readStream(channel, handler);
            } catch (StreamException ex) {
                if (ex.getCause() != null && (ex.getCause() instanceof java.io.InterruptedIOException)) {
                    ex.printStackTrace();
                    throw (java.io.InterruptedIOException) ex.getCause();
                } else {
                    ex.printStackTrace();
                    throw ex;
                }
            }
            mon.finished();
            DataSet ds = handler.getDataSet();
            if (ds == null) {
                throw new RuntimeException("failed to get dataset, without explanation!  (Possibly no records)");
            }
            if (ds.getXLength() == 0) {
                throw new RuntimeException("empty dataset returned");
            }
            AbstractDataSet result;
            if (item == null || item.equals("") || item.equals("0")) {
                result = DataSetAdapter.create(ds);
            } else {
                DataSet das2ds;
                das2ds = ds.getPlanarView(item);
                if (das2ds == null) {
                    int iitem = Integer.parseInt(item);
                    das2ds = ds.getPlanarView("plane_" + iitem);
                }
                if (das2ds == null) throw new IllegalArgumentException("no such plane, looking for " + item);
                result = DataSetAdapter.create(das2ds);
            }
            if (tcaDesc != null && tcaDesc.size() > 0) {
                if (item == null) {
                    result.putProperty(QDataSet.LABEL, tcaDesc.get(0));
                } else {
                    result.putProperty(QDataSet.LABEL, tcaDesc.get(Integer.parseInt(item)));
                }
            }
            result1 = result;
        }
        if (timeRange == null) timeRange = new DatumRange(Units.us2000.parse(params2.get("start_time")), Units.us2000.parse(params2.get("end_time")));
        logger.fine("  done. ");
        try {
            String prop = QDataSet.DEPEND_0;
            QDataSet dep = (QDataSet) result1.property(prop);
            if (dep == null) {
                prop = QDataSet.JOIN_0;
                Object o = result1.property(prop);
                if (o instanceof QDataSet) {
                    dep = (QDataSet) o;
                }
            }
            if (dep != null && dep.property(QDataSet.CACHE_TAG) == null) {
                QDataSet bounds = SemanticOps.bounds(result1);
                CacheTag ct = new CacheTag(DataSetUtil.asDatumRange(bounds.slice(0), true), resolution);
                MutablePropertyDataSet dep2 = DataSetOps.makePropertiesMutable(dep);
                dep2.putProperty(QDataSet.CACHE_TAG, ct);
                MutablePropertyDataSet result2 = DataSetOps.makePropertiesMutable(result1);
                result2.putProperty(prop, dep2);
                return result2;
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return result1;
    }
