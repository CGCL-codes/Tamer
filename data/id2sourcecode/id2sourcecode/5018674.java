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
                            if (xyz) {
                                plt.println("X " + qnumData + " " + qmean1 + " " + qstd1);
                                plt.println("Y " + qnumData + " " + qmean2 + " " + qstd2);
                                plt.println("Z " + qnumData + " " + qmean3 + " " + qstd3);
                            } else if (dhz) {
                                plt.println("D " + qnumData + " " + qmean1 + " " + qstd1);
                                plt.println("H " + qnumData + " " + qmean2 + " " + qstd2);
                                plt.println("Z " + qnumData + " " + qmean3 + " " + qstd3);
                            } else if (ehz) {
                                plt.println("E " + qnumData + " " + qmean1 + " " + qstd1);
                                plt.println("H " + qnumData + " " + qmean2 + " " + qstd2);
                                plt.println("Z " + qnumData + " " + qmean3 + " " + qstd3);
                            }
                            for (int k = 0; k < numData; k++) {
                                String dstr = df.format(utc.getTime());
                                plt.print(dstr.substring(2, dstr.length()));
                                plt.print(stnCode + " ");
                                if (xyz) plt.print("XYZ "); else if (dhz) plt.print("DHZ "); else if (ehz) plt.print("EHZ ");
                                if (data1 != null && !Float.isNaN(data1[k]) && data1[k] != missval) plt.print(data1[k] + " "); else plt.print("-99999 ");
                                if (data2 != null && !Float.isNaN(data2[k]) && data2[k] != missval) plt.print(data2[k] + " "); else plt.print("-99999 ");
                                if (data3 != null && !Float.isNaN(data3[k]) && data3[k] != missval) plt.print(data3[k] + " "); else plt.print("-99999 ");
                                if (!Float.isNaN(std1) && std1 != missval) plt.print(std1 + " "); else plt.print("-99999 ");
                                if (!Float.isNaN(std2) && std2 != missval) plt.print(std2 + " "); else plt.print("-99999 ");
                                if (!Float.isNaN(std3) && std3 != missval) plt.print(std3 + " "); else plt.print("-99999 ");
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

    /** Creates input files for AMIE runs from the vector of quiet day subtracted
   *  daily observations (one DailyData object per station per component)
   *  @param subRecords The Vector of DailyData objects
   *  @return URL to the exported data file
   */
    public static String exportGeomagData(Vector subRecords, String fileName) throws Exception {
        if (subRecords == null || subRecords.size() == 0 || fileName == null) return null;
        WDCDay day = null;
        String sectionName = null;
        String sectionPrefix = null;
        String exportPath = Settings.get("locations.localExportDir");
        String importPath = Settings.get("locations.httpExportDir");
        if (subRecords.size() > 0) {
            FileOutputStream fos = new FileOutputStream(exportPath + fileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            PrintWriter plt = null;
            DateInterval dateInt = findDateInterval(subRecords);
            log.info("SWRDataBean.exportGeomagData(): dateIntervalDescr=\"" + dateInt.toNiceString() + "\";");
            sectionPrefix = "" + dateInt.getDateFrom().getDayId();
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
                            int qnumData = -99999;
                            float qstd1 = -99999, qstd2 = -99999, qstd3 = -99999;
                            float qmean1 = -99999, qmean2 = -99999, qmean3 = -99999;
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
