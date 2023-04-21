    @SuppressWarnings("unchecked")
    public String createReportChart(String inputFile, String title, String chartType, int countNum) {
        java.io.BufferedReader br = null;
        java.io.OutputStream bout = null;
        StringBuilder result = new StringBuilder();
        String pngFilePath = topAnalyzerConfig.getChartFilePath();
        if (title == null || (title != null && "".equals(title))) title = inputFile;
        String[] colors = new String[] { "00ff00", "ff0000", "01ff4f", "ffffa2", "00ffff", "afff0f", "f000ff", "fa0b0f", "0000ff", "f0f0f0", "0f0f0f" };
        try {
            File file = new File(inputFile);
            if (!file.exists()) throw new java.lang.RuntimeException("chart file not exist : " + inputFile);
            if (pngFilePath.endsWith(File.separator)) {
                pngFilePath = pngFilePath + file.getName().substring(0, file.getName().indexOf("_")) + ".png";
            } else pngFilePath = pngFilePath + File.separator + file.getName().substring(0, file.getName().indexOf("_")) + ".png";
            new File(pngFilePath).createNewFile();
            bout = new FileOutputStream(new File(pngFilePath));
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb2312"));
            String line = null;
            int num = 0;
            if (chartType != null && chartType.startsWith("bar")) {
                String type;
                if (chartType.equals("bar")) type = "bvg&chbh=a"; else type = chartType.substring(4);
                result.append("http://chart.apis.google.com/chart?chs=700x400&cht=").append(type).append("&chtt=").append(URLEncoder.encode(title, "utf-8")).append("&chxt=x,y&chg=0,10");
            } else result.append("http://chart.apis.google.com/chart?chs=700x400&cht=lxy&chtt=").append(URLEncoder.encode(title, "utf-8")).append("&chxt=x,y&chg=0,10");
            StringBuilder chartDataLabel = new StringBuilder().append("chdl=");
            StringBuilder chartColor = new StringBuilder().append("chco=");
            StringBuilder chartXLabel = new StringBuilder().append("chxl=0:");
            StringBuilder chartXPosition = new StringBuilder().append("chxp=0,");
            StringBuilder chartXRange = new StringBuilder().append("chxr=0,0,");
            StringBuilder chartYRange = new StringBuilder().append("|1,0,");
            StringBuilder chartData = new StringBuilder().append("chd=t:");
            DecimalFormat dformater = new DecimalFormat("0.00");
            double yMax = 0;
            List<String>[] rList = null;
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] contents = line.split(",");
                if (num == 0) {
                    rList = new List[contents.length];
                    for (int i = 0; i < contents.length; i++) {
                        rList[i] = new ArrayList<String>();
                        if (i != contents.length - 1) {
                            if (i > 0) {
                                chartDataLabel.append(URLEncoder.encode(contents[i], "utf-8")).append("|");
                                chartColor.append(colors[i % colors.length]).append(",");
                            }
                        } else {
                            chartDataLabel.append(URLEncoder.encode(contents[i], "utf-8"));
                            chartColor.append(colors[i % colors.length]);
                        }
                    }
                } else {
                    if (countNum > 0 && index >= countNum) break;
                    index += 1;
                    if (contents[0].indexOf(" ") > 0) {
                        String[] cs = contents[0].split(" ");
                        chartXLabel.append("|").append(cs[cs.length - 1]);
                    } else chartXLabel.append("|").append(contents[0]);
                    for (int i = 0; i < contents.length; i++) {
                        if (i == 0) {
                            rList[i].add(String.valueOf(num));
                        } else {
                            double y = Double.valueOf(contents[i]);
                            if (yMax < y) yMax = y;
                            rList[i].add(contents[i]);
                        }
                    }
                }
                num += 1;
            }
            if (rList != null && rList.length > 1) {
                if (yMax <= 100) chartYRange.append(100); else chartYRange.append(yMax);
                StringBuilder x = new StringBuilder();
                for (int j = 0; j < rList[0].size(); j++) {
                    x.append(dformater.format(Double.valueOf(rList[0].get(j)) * ((double) 100 / (double) num)));
                    chartXPosition.append(rList[0].get(j));
                    if (j < rList[0].size() - 1) {
                        x.append(",");
                        chartXPosition.append(",");
                    }
                }
                chartXRange.append(num);
                for (int i = 1; i < rList.length; i++) {
                    chartData.append(x).append("|");
                    for (int j = 0; j < rList[i].size(); j++) {
                        if (yMax <= 100) {
                            if (j < rList[i].size() - 1) {
                                chartData.append(dformater.format(Double.valueOf(rList[i].get(j)))).append(",");
                            } else {
                                chartData.append(dformater.format(Double.valueOf(rList[i].get(j)))).append("|");
                            }
                        } else {
                            double y = Double.valueOf(rList[i].get(j));
                            if (j < rList[i].size() - 1) {
                                chartData.append(dformater.format(y / yMax * 100)).append(",");
                            } else {
                                chartData.append(dformater.format(y / yMax * 100)).append("|");
                            }
                        }
                    }
                }
                if (chartData.toString().endsWith("|")) chartData.deleteCharAt(chartData.toString().length() - 1);
                result.append("&").append(chartDataLabel).append("&").append(chartColor).append("&").append(chartXLabel).append("&").append(chartXPosition).append("&").append(chartXRange).append(chartYRange).append("&").append(chartData);
                logger.info(result.toString());
                URL url = new URL(result.toString());
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream cin = connection.getInputStream();
                byte[] buf = new byte[1000];
                int count = 0;
                while ((count = cin.read(buf)) > 0) {
                    bout.write(buf, 0, count);
                }
            } else {
                logger.error("no result export...");
                return "";
            }
        } catch (Exception ex) {
            logger.error(ex, ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error(e, e);
                }
            }
            if (bout != null) {
                try {
                    bout.close();
                } catch (IOException e) {
                    logger.error(e, e);
                }
            }
        }
        if (pngFilePath.indexOf(File.separator) >= 0) {
            try {
                pngFilePath = URLEncoder.encode(pngFilePath.substring((pngFilePath.lastIndexOf(File.separator)) + 1, pngFilePath.length()), "utf-8");
            } catch (Exception ex) {
                logger.error(ex, ex);
            }
        }
        return pngFilePath;
    }
