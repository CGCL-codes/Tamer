    public void run(String line, BasicTerminalIO tio, Connection connection, AuthInfo authInfo) throws IOException, ShutdownException, StopException {
        Matcher m = cmdPattern.matcher(line);
        if (!m.matches()) {
            tio.write(ColorHelper.colorizeText("Wrong command format\n", ColorHelper.RED));
        } else {
            String idx = m.group(1);
            String url = m.group(2);
            String fn = m.group(3);
            int index = -1;
            if (!Validator.isHardEmpty(idx)) {
                try {
                    index = Integer.parseInt(idx);
                } catch (NumberFormatException ex) {
                    tio.write(ColorHelper.colorizeText("Wrong position in queue specified\n", ColorHelper.RED));
                    tio.flush();
                    return;
                }
            }
            try {
                url = trimAndRemoveQuotes(url);
                if (log.isDebugEnabled()) log.debug(MessageFormat.format("Adding download with UTL \"{0}\", and file name \"{1}\"", url, fn));
                int id = DownloadManager.queueDownload(url, (Validator.isHardEmpty(fn) ? null : fn), authInfo.getUser(), index);
                tio.write(MessageFormat.format("Download added to queue by id {0}\n", id));
                log.info(MessageFormat.format("Download added to queue by id {0}", id));
            } catch (URIException ex) {
                tio.write(ColorHelper.colorizeText(MessageFormat.format("Bad URL: \"{0}\"\n", url), ColorHelper.RED));
            } catch (SchemeNotSupported ex) {
                tio.write(ColorHelper.colorizeText("Wrong url specified\n", ColorHelper.RED));
            } catch (DownloadAlreadyExist ex) {
                tio.write(ColorHelper.colorizeText("Specified URL already downloading\n", ColorHelper.RED));
            } catch (Throwable ex) {
                tio.write(ColorHelper.colorizeText("Unknown error\n", ColorHelper.RED));
                log.error("Unknown error by adding download ", ex);
            }
        }
        tio.flush();
    }
