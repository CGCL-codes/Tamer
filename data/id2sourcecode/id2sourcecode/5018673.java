        if (dailyDescr == null || quietDescr == null) return null;
        log.debug("SWRDataBean.subtract(): daily element is " + dailyDescr.getElement());
        log.debug("SWRDataBean.subtract(): quiet element is " + quietDescr.getElement());
        if (!dailyDescr.getElement().equalsIgnoreCase(quietDescr.getElement())) return null;
        float missval = dailyDescr.getMissingValue();
        log.debug("SWRDataBean.subtract(): missing value is " + missval);
        float[] ddata = dailyData.getData();
        float[] qdata = quietData.getData();
        if (ddata == null || qdata == null || ddata.length != qdata.length) {
            log.debug("SWRDataBean.subtract(): different data lengths in daily and quiet elements " + ddata.length + " compared with " + qdata.length);
            return null;
        }
        float[] average = new float[qdata.length];
        float[] result = new float[ddata.length];
        float mean = 0;
        int mcount = 0;
        average[0] = qdata[0];
        for (int i = 1; i < qdata.length; i++) {
            if (qdata[i] != missval) {
                mean += qdata[i];
                mcount++;
            }
            int count = 0;
            float sum = 0;
            for (int j = 0; j < AVERAGE_WINDOW && i - j >= 0; j++) {
                if (qdata[i - j] != missval) {
                    sum += qdata[i - j];
                    count++;
                }
            }
            if (count > 0) average[i] = sum / count; else average[i] = missval;
        }
        if (mcount > 0) mean = mean / mcount; else mean = missval;
        if (mean == missval) {
            log.error("SWRDataBean.subtract(): quiet data all missing");
            return null;
        }
        int goodCount = 0;
        if (Math.abs(lat) > 50) {
            for (int i = 0; i < ddata.length; i++) {
                if (Float.isNaN(ddata[i]) || ddata[i] == missval || average[i] == missval) result[i] = missval; else {
                    result[i] = ddata[i] - average[i];
                    goodCount++;
                }
            }
        } else {
            for (int i = 0; i < ddata.length; i++) {
                if (Float.isNaN(ddata[i]) || ddata[i] == missval) result[i] = missval; else {
                    result[i] = ddata[i] - mean;
                    goodCount++;
                }
            }
        }
        log.info("SWRDataBean.subtract(): made " + goodCount + " real subtractions");
        DailyData subData = new DailyData(dailyData.getDayId(), result, dailyDescr, dailyStn);
        return subData;
    }

    /** Creates input files for AMIE runs from the vector of quiet day subtracted
   *  daily observations (one DailyData object per station per component)
   *  @param subRecords The Vector of DailyData objects
   *  @timestep sampling interval in minutes
   *  @return URL to the exported data file
   */
    public static String exportGeomagData(Vector indexRecords, Vector subRecords, Vector quietRecords, String fileName) throws Exception {
        if (indexRecords == null || subRecords == null || subRecords.size() == 0 || quietRecords == null || fileName == null) return null;
        WDCDay day = null;
        String sectionName = null;
        String sectionPrefix = null;
        String exportPath = Settings.get("locations.localExportDir");
        String importPath = Settings.get("locations.httpExportDir");
        if (indexRecords.size() > 0 && subRecords.size() > 0 && quietRecords.size() > 0) {
            FileOutputStream fos = new FileOutputStream(exportPath + fileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            PrintWriter plt = null;
            DateInterval dateInt = findDateInterval(subRecords);
            log.info("SWRDataBean.exportGeomagData(): dateIntervalDescr=\"" + dateInt.toNiceString() + "\";");
            DataSequenceSet dss = null;
            if (indexRecords != null) dss = UpdateMetadata.dailyRecordsToDSS(indexRecords);
            day = dateInt.getDateFrom();
            sectionPrefix = "" + day.getDayId();
            sectionName = sectionPrefix + "indices";
            zos.putNextEntry(new ZipEntry(sectionName + ".amie"));
            plt = new PrintWriter(zos);
            log.info("SWRDataBean.exportGeomagData(): new archive section " + sectionName);
            plt.println("#Spidr data output file");
            plt.println("#GMT time is used");
            plt.println("#");
            plt.println("#");
            plt.println("#--------------------------------------------------");
            for (int i = 0; i < dss.size(); i++) DSSExport.vec2stream(plt, (DataSequence) dss.elementAt(i));
            plt.flush();
            zos.closeEntry();
            Enumeration enumer = subRecords.elements();
            while (enumer.hasMoreElements()) {
                DailyData dailyd = (DailyData) enumer.nextElement();
                if (dailyd != null) {
                    SwrStation stn = (SwrStation) dailyd.getStation();
                    String stnCode = null;
                    String stnType = null;
                    if (stn != null) {
                        stnCode = stn.getStn();
                        stnType = stn.getDataType();
                    }
                    if (stnCode != null) {
                        float declination = stn.getDeclination();
                        String element = dailyd.getDescription().getElement();
                        if ("Z".equalsIgnoreCase(element)) {
                            log.info("SWRDataBean.exportGeomagData(): got z component for station " + stnCode + " type " + stnType);
                            Enumeration qenum = quietRecords.elements();
                            boolean qdayOk = true;
                            int qnumData = -99999;
                            float qstd1 = -99999, qstd2 = -99999, qstd3 = -99999;
                            float qmean1 = -99999, qmean2 = -99999, qmean3 = -99999;
                            while (qenum.hasMoreElements()) {
                                DailyData qdailyd = (DailyData) qenum.nextElement();
                                if (qdailyd != null) {
                                    SwrStation qstn = (SwrStation) qdailyd.getStation();
                                    String qstnCode = null;
                                    String qstnType = null;
                                    if (qstn != null) {
                                        qstnCode = qstn.getStn();
                                        qstnType = qstn.getDataType();
                                    }
                                    if (qstnCode != null && qstnCode.equalsIgnoreCase(stnCode)) {
                                        String qelement = qdailyd.getDescription().getElement();
                                        if ("Z".equalsIgnoreCase(qelement)) {
                                            log.info("SWRDataBean.exportGeomagData(): got z quiet day component for station " + stnCode);
                                            DailyData dz = qdailyd;
                                            DailyData dx = null;
                                            DailyData dy = null;
                                            DailyData dd = null;
                                            DailyData dh = null;
                                            DailyData de = null;
                                            Enumeration enumxyz = quietRecords.elements();
                                            while (enumxyz.hasMoreElements()) {
                                                DailyData dtmp = (DailyData) enumxyz.nextElement();
                                                Station stnTmp = dtmp.getStation();
                                                String stnCodeTmp = null;
                                                if (stnTmp != null && stnCode.equals(stnTmp.getStn())) {
                                                    String elementTmp = dtmp.getDescription().getElement();
                                                    log.debug("SWRDataBean.exportGeomagData(): found quiet component " + elementTmp);
                                                    if ("X".equalsIgnoreCase(elementTmp)) dx = dtmp; else if ("Y".equalsIgnoreCase(elementTmp)) dy = dtmp; else if ("D".equalsIgnoreCase(elementTmp)) if ("EHZ".equalsIgnoreCase(qstnType)) {
                                                        de = dtmp;
                                                        log.info("SWRDataBean.exportGeomagData(): quiet D component in fact is E in nT");
                                                    } else dd = dtmp; else if ("H".equalsIgnoreCase(elementTmp)) dh = dtmp; else if ("E".equalsIgnoreCase(elementTmp)) de = dtmp;
                                                }
                                            }
                                            boolean qxyz = (dx != null && dy != null && dz != null);
                                            boolean qdhz = (dd != null && dh != null && dz != null);
                                            boolean qehz = (de != null && dh != null && dz != null);
                                            int sampling = dz.getSampling();
                                            float missval = dz.getDescription().getMissingValue();
                                            float[] data1 = null;
                                            float[] data2 = null;
                                            float[] data3 = dz.getData();
                                            if (qxyz) {
                                                data1 = dx.getData();
                                                data2 = dy.getData();
                                                data3 = dz.getData();
                                            } else if (qdhz) {
                                                data1 = dd.getData();
                                                data2 = dh.getData();
                                                data3 = dz.getData();
                                            } else if (qehz) {
                                                data1 = de.getData();
                                                data2 = dh.getData();
                                                data3 = dz.getData();
                                            } else {
                                                log.info("SWRDataBean.exportGeomagData(): unknown quiet day station type - station was skipped");
                                                qdayOk = false;
                                                break;
                                            }
                                            if (data1 == null || data2 == null || data3 == null) {
                                                log.info("SWRDataBean.exportGeomagData(): one of data segments in quiet day data is null - station was skipped");
                                                qdayOk = false;
                                                break;
                                            }
                                            qnumData = data3.length;
                                            qmean1 = findMedian(data1, missval);
                                            qmean2 = findMedian(data2, missval);
                                            qmean3 = findMedian(data3, missval);
                                            if (qmean1 == missval || qmean2 == missval || qmean3 == missval) {
                                                log.info("SWRDataBean.exportGeomagData(): one of data segments in quiet day data is empty - station was skipped");
                                                qdayOk = false;
                                                break;
                                            }
                                            qstd1 = findStd(data1, missval);
                                            qstd2 = findStd(data2, missval);
                                            qstd3 = findStd(data3, missval);
                                        }
                                    }
                                }
                            }
                            if (!qdayOk) continue;
                            DailyData dz = dailyd;
                            DailyData dx = null;
                            DailyData dy = null;
                            DailyData dd = null;
                            DailyData dh = null;
                            DailyData de = null;
                            Enumeration enumxyz = subRecords.elements();
                            while (enumxyz.hasMoreElements()) {
                                DailyData dtmp = (DailyData) enumxyz.nextElement();
                                Station stnTmp = dtmp.getStation();
                                String stnCodeTmp = null;
                                if (stnTmp != null && stnCode.equals(stnTmp.getStn())) {
                                    String elementTmp = dtmp.getDescription().getElement();
                                    log.debug("SWRDataBean.exportGeomagData(): found component " + elementTmp);
                                    if ("X".equalsIgnoreCase(elementTmp)) dx = dtmp; else if ("Y".equalsIgnoreCase(elementTmp)) dy = dtmp; else if ("D".equalsIgnoreCase(elementTmp)) if ("EHZ".equalsIgnoreCase(stnType)) {
                                        de = dtmp;
                                        log.info("SWRDataBean.exportGeomagData(): daily D component in fact is E in nT");
                                    } else dd = dtmp; else if ("H".equalsIgnoreCase(elementTmp)) dh = dtmp; else if ("E".equalsIgnoreCase(elementTmp)) de = dtmp;
                                }
                            }
                            boolean xyz = (dx != null && dy != null && dz != null);
                            boolean dhz = (dd != null && dh != null && dz != null);
                            boolean ehz = (de != null && dh != null && dz != null);
                            TimeZone tz = new SimpleTimeZone(0, "GMT");
                            Calendar utc = new GregorianCalendar(tz);
                            day = new WDCDay(dz.getDayId());
                            utc.set(day.getYear(), day.getMonth() - 1, day.getDay(), 0, 0, 0);
                            utc.set(Calendar.MILLISECOND, 0);
                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
                            df.setTimeZone(tz);
                            df.setCalendar(utc);
                            int sampling = dz.getSampling();
                            float missval = dz.getDescription().getMissingValue();
                            float[] data1 = null;
                            float[] data2 = null;
                            float[] data3 = null;
                            if (xyz) {
                                data1 = dx.getData();
                                data2 = dy.getData();
                                data3 = dz.getData();
                            } else if (dhz) {
                                data1 = dd.getData();
                                data2 = dh.getData();
                                data3 = dz.getData();
                            } else if (ehz) {
                                data1 = de.getData();
                                data2 = dh.getData();
                                data3 = dz.getData();
                            } else {
                                log.info("SWRDataBean.exportGeomagData(): unknown daily data station type - station was skipped");
                                continue;
                            }
                            int numData = 0;
                            if (data3 == null) {
                                log.info("SWRDataBean.exportGeomagData(): data3 segment in daily data is null");
