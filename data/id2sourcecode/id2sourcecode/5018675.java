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
                                if (data2 == null) {
                                    log.info("SWRDataBean.exportGeomagData(): data2 segment in daily data is null");
                                    if (data1 == null) {
                                        log.info("SWRDataBean.exportGeomagData(): data1 segment in daily data is null - station was skipped");
                                        continue;
                                    } else {
                                        numData = data1.length;
                                    }
                                } else {
                                    numData = data2.length;
                                }
                            } else {
                                numData = data3.length;
                            }
                            float mean1 = findMean(data1, missval);
                            float mean2 = findMean(data2, missval);
                            float mean3 = findMean(data3, missval);
                            if (mean1 == missval || mean2 == missval || mean3 == missval) {
                                log.info("SWRDataBean.exportGeomagData(): one of data segments in daily data is empty");
                            }
                            float std1 = findStd(data1, missval);
                            float std2 = findStd(data2, missval);
                            float std3 = findStd(data3, missval);
                            sectionName = sectionPrefix + stnCode;
                            zos.putNextEntry(new ZipEntry(sectionName + ".amie"));
                            plt = new PrintWriter(zos);
                            log.debug("SWRDataBean.exportGeomagData(): new archive section " + sectionName);
                            for (int k = 0; k < numData; k++) {
                                String dstr = df.format(utc.getTime());
                                plt.print(dstr.substring(2, dstr.length()));
                                plt.print(stnCode + " ");
                                if (xyz) plt.print("XYZ"); else if (dhz) plt.print("DHZ"); else if (ehz) plt.print("EHZ");
                                spidr.swr.Format format = new spidr.swr.Format("%10.2f");
                                if (data1 != null && !Float.isNaN(data1[k]) && data1[k] != missval) plt.print(format.form(data1[k])); else plt.print(format.form(-99999f));
                                if (data2 != null && !Float.isNaN(data2[k]) && data2[k] != missval) plt.print(format.form(data2[k])); else plt.print(format.form(-99999f));
                                if (data3 != null && !Float.isNaN(data3[k]) && data3[k] != missval) plt.print(format.form(data3[k])); else plt.print(format.form(-99999f));
                                plt.println("");
                                utc.add(Calendar.MINUTE, sampling);
                            }
                            plt.flush();
                            zos.closeEntry();
                        }
                    }
                }
            }
            zos.finish();
        }
        return importPath + fileName;
    }

    /** Creates input files for KRM runs from the vector of quiet day subtracted
   *  daily observations (one DailyData object per station per component)
   *  @param subRecords The Vector of DailyData objects
   *  @timestep sampling interval in minutes
   *  @return URL to the exported data file
   */
    public static String exportGeomagDataKrm(Vector subRecords, String fileName) throws Exception {
        if (subRecords == null || subRecords.size() == 0 || fileName == null) return null;
        WDCDay day = null;
        String sectionName = null;
        String exportPath = Settings.get("locations.localExportDir");
        String importPath = Settings.get("locations.httpExportDir");
        DateInterval dateInt = findDateInterval(subRecords);
        log.debug("SWRDataBean.exportGeomagDataKrm(): dateIntervalDescr=\"" + dateInt.toNiceString() + "\";");
        int sampling = 0;
        int numData = 0;
        HashMap stnHash = new HashMap();
        HashMap xyzHash = new HashMap();
        Enumeration enumer = subRecords.elements();
        while (enumer.hasMoreElements()) {
            DailyData dailyd = (DailyData) enumer.nextElement();
            if (dailyd != null) {
                SwrStation stn = (SwrStation) dailyd.getStation();
                String stnCode = null;
                String stnType = null;
                if (stn != null) {
                    stnCode = stn.getStn();
                    if (stnCode != null) {
                        String element = dailyd.getDescription().getElement();
                        if ("Z".equalsIgnoreCase(element)) {
                            log.debug("SWRDataBean.exportGeomagDataKrm(): got z component for station " + stnCode + " type " + stnType);
                            DailyData dz = dailyd;
                            DailyData dx = null;
                            DailyData dy = null;
                            int sampTmp = dz.getSampling();
                            if (sampling == 0) {
                                log.debug("SWRDataBean.exportGeomagDataKrm(): data sampling is " + sampTmp);
                                sampling = sampTmp;
                            } else if (sampling != sampTmp) {
                                log.info("SWRDataBean.exportGeomagDataKrm(): z component has different sampling " + sampTmp);
                                continue;
                            }
                            Enumeration enumxyz = subRecords.elements();
                            while (enumxyz.hasMoreElements()) {
                                DailyData dtmp = (DailyData) enumxyz.nextElement();
                                Station stnTmp = dtmp.getStation();
                                String stnCodeTmp = null;
                                if (stnTmp != null && stnCode.equals(stnTmp.getStn())) {
                                    String elementTmp = dtmp.getDescription().getElement();
                                    log.debug("SWRDataBean.exportGeomagDataKrm(): found component " + elementTmp);
                                    if ("X".equalsIgnoreCase(elementTmp)) dx = dtmp; else if ("Y".equalsIgnoreCase(elementTmp)) dy = dtmp;
                                }
                            }
                            boolean xyz = (dx != null && dy != null && dz != null);
                            if (xyz) {
                                float[] data1 = dx.getData();
                                float[] data2 = dy.getData();
                                float[] data3 = dz.getData();
                                int numDataTmp = data3.length;
                                if (numData == 0) {
                                    log.info("SWRDataBean.exportGeomagDataKrm(): data length is " + numDataTmp);
                                    numData = numDataTmp;
                                } else if (data1.length != numData) {
                                    log.info("SWRDataBean.exportGeomagDataKrm(): x component has different length " + data1.length);
                                    continue;
                                } else if (data2.length != numData) {
                                    log.info("SWRDataBean.exportGeomagDataKrm(): y component has different length " + data2.length);
                                    continue;
                                } else if (data3.length != numData) {
                                    log.info("SWRDataBean.exportGeomagDataKrm(): z component has different length " + data3.length);
                                    continue;
