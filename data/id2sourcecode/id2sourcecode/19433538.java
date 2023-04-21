    public void run(String line, BasicTerminalIO tio, Connection connection, AuthInfo authInfo) throws IOException, ShutdownException, StopException {
        Matcher m = cmdPattern.matcher(line);
        if (!m.matches()) {
            tio.write(ColorHelper.colorizeText("Wrong command format\n", ColorHelper.RED));
        } else {
            String strOrderNum = m.group(1);
            String strUrlTemplate = m.group(2);
            char strTemplateSymbol = m.group(3).charAt(0);
            String strBeginNumber = m.group(4);
            String strEndNumber = m.group(5);
            int index = -1;
            if (!Validator.isHardEmpty(strOrderNum)) {
                try {
                    index = Integer.parseInt(strOrderNum);
                } catch (NumberFormatException ex) {
                    tio.write(ColorHelper.colorizeText("Wrong position in queue specified\n", ColorHelper.RED));
                    tio.flush();
                    return;
                }
            }
            strUrlTemplate = trimAndRemoveQuotes(strUrlTemplate);
            if (log.isDebugEnabled()) log.debug(MessageFormat.format("Specified follow URL template: {0}", strUrlTemplate));
            long beginNumber = -1;
            long endNumber = -1;
            if (!Validator.isHardEmpty(strBeginNumber)) {
                try {
                    beginNumber = Long.parseLong(strBeginNumber);
                } catch (NumberFormatException ex) {
                    tio.write(ColorHelper.colorizeText("Wrong begin number of range specified\n", ColorHelper.RED));
                    tio.flush();
                    log.debug("Wrong begin number of range specified");
                    return;
                }
            }
            if (!Validator.isHardEmpty(strEndNumber)) {
                try {
                    endNumber = Long.parseLong(strEndNumber);
                } catch (NumberFormatException ex) {
                    tio.write(ColorHelper.colorizeText("Wrong end number of range specified\n", ColorHelper.RED));
                    tio.flush();
                    log.debug("Wrong end number of range specified");
                    return;
                }
            }
            if (beginNumber < 0 || endNumber < 0 || beginNumber > endNumber) {
                tio.write(ColorHelper.colorizeText("Wrong bounds of range specified\n", ColorHelper.RED));
                tio.flush();
                log.debug("Wrong bounds of range specified");
                return;
            }
            if (endNumber - beginNumber > 1000) {
                tio.write(ColorHelper.colorizeText("Range size must be less or equal 1000\n", ColorHelper.RED));
                tio.flush();
                log.debug("Range size must be less or equal 1000");
                return;
            }
            int wideOfNumber = 0;
            int firstIndex = strUrlTemplate.indexOf(strTemplateSymbol);
            if (firstIndex < 0) {
                tio.write(ColorHelper.colorizeText("Template symbol in URL template not found\n", ColorHelper.RED));
            } else {
                for (int i = firstIndex; i < strUrlTemplate.length(); i++) {
                    if (strUrlTemplate.charAt(i) == strTemplateSymbol) wideOfNumber++; else break;
                }
                StringBuilder sbuf = new StringBuilder();
                for (int i = 0; i < wideOfNumber; i++) sbuf.append('0');
                DecimalFormat numberFormat = new DecimalFormat(sbuf.toString());
                String regex = strTemplateSymbol + "{" + wideOfNumber + '}';
                int countOfDownloads = 0;
                for (long i = beginNumber; i <= endNumber; i++) {
                    String resUrl = strUrlTemplate.replaceAll(regex, numberFormat.format(i));
                    if (log.isDebugEnabled()) log.debug(MessageFormat.format("Adding follow URL (from template): {0}", resUrl));
                    try {
                        int id = DownloadManager.queueDownload(resUrl, null, authInfo.getUser(), index);
                        countOfDownloads++;
                        if (index > 0) index++;
                        tio.write(MessageFormat.format("Download part {1} added to queue by id {0}\n", id, i));
                        log.info(MessageFormat.format("Download part {1} added to queue by id {0}", id, i));
                    } catch (URIException ex) {
                        tio.write(ColorHelper.colorizeText(MessageFormat.format("Bad URL: \"{0}\"\n", resUrl), ColorHelper.RED));
                        log.warn(MessageFormat.format("Bad URL: \"{0}\"\n", resUrl));
                        if (i == beginNumber) {
                            log.warn("Stop adding URLs from template");
                            break;
                        }
                    } catch (SchemeNotSupported ex) {
                        tio.write(ColorHelper.colorizeText("Wrong url specified\n", ColorHelper.RED));
                        break;
                    } catch (DownloadAlreadyExist ex) {
                        tio.write(ColorHelper.colorizeText(MessageFormat.format("Specified URL {0} already downloading\n", resUrl), ColorHelper.RED));
                        if (i == beginNumber) {
                            log.warn("Stop adding URLs from template");
                            break;
                        }
                    } catch (Throwable ex) {
                        tio.write(ColorHelper.colorizeText("Unknown error\n", ColorHelper.RED));
                        log.error("Unknown error by adding download ", ex);
                        break;
                    }
                }
            }
        }
        tio.flush();
    }
