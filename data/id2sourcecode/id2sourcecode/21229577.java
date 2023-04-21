    public void write(LogEntry logEntry) throws IOException {
        SimpleValidator.validateNotNull(logEntry, "logEntry");
        String logId = logEntry.getLogId() == null ? "" : logEntry.getLogId();
        synchronized (this.logger) {
            write(logId, LogType.DATE, logEntry.getDate(), false);
            write(logId, LogType.TIME, logEntry.getTime(), false);
            write(logId, LogType.THREAD_ID, logEntry.getThreadId(), false);
            write(logId, LogType.THREAD_NAME, logEntry.getThreadName(), true);
            write(logId, LogType.METHOD, logEntry.getMethod(), false);
            write(logId, LogType.REQUEST_URL, logEntry.getRequestUrl(), true);
            write(logId, LogType.PROTOCOL, logEntry.getProtocol(), true);
            writeArrayMap(logId, LogType.REQUEST_PARAMETERS, logEntry.getRequestParameters());
            writeListMap(logId, LogType.REQUEST_HEADERS, logEntry.getRequestHeaders());
            writeCookies(logId, LogType.REQUEST_COOKIES, logEntry.getRequestCookies());
            write(logId, LogType.REQUEST_CONTENT_LENGTH, logEntry.getRequestContentLength(), false);
            write(logId, LogType.REQUEST_CHARACTER_ENCODING, logEntry.getRequestCharacterEncoding(), false);
            write(logId, LogType.REQUEST_CONTENT_TYPE, logEntry.getRequestContentType(), true);
            write(logId, LogType.REMOTE_ADDR, logEntry.getRemoteAddr(), false);
            write(logId, LogType.REMOTE_HOST, logEntry.getRemoteHost(), false);
            write(logId, LogType.REMOTE_PORT, logEntry.getRemotePort(), false);
            writeList(logId, LogType.REQUEST_LOCALES, logEntry.getRequestLocales());
            write(logId, LogType.AUTH_TYPE, logEntry.getAuthType(), true);
            write(logId, LogType.USER_PRINCIPAL, logEntry.getUserPrincipal(), true);
            write(logId, LogType.SESSION_ID, logEntry.getSessionId(), true);
            this.logger.flush();
            LogMode logMode = LogMode.getInstance();
            try {
                writeAttributes(logId, LogType.REQUEST_ATTRIBUTES_BEFORE, logEntry.getRequestAttributesBefore(), logMode.getRequestAttributeDescribeModeAsEnum());
                writeAttributes(logId, LogType.REQUEST_ATTRIBUTES_AFTER, logEntry.getRequestAttributesAfter(), logMode.getRequestAttributeDescribeModeAsEnum());
                writeAttributes(logId, LogType.SESSION_ATTRIBUTES_BEFORE, logEntry.getSessionAttributesBefore(), logMode.getSessionAttributeDescribeModeAsEnum());
                writeAttributes(logId, LogType.SESSION_ATTRIBUTES_AFTER, logEntry.getSessionAttributesAfter(), logMode.getSessionAttributeDescribeModeAsEnum());
                writeAttributes(logId, LogType.CONTEXT_ATTRIBUTES_BEFORE, logEntry.getContextAttributesBefore(), logMode.getContextAttributeDescribeModeAsEnum());
                writeAttributes(logId, LogType.CONTEXT_ATTRIBUTES_AFTER, logEntry.getContextAttributesAfter(), logMode.getContextAttributeDescribeModeAsEnum());
            } catch (IllegalArgumentException e) {
                throw new IOException(e);
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            } catch (InvocationTargetException e) {
                throw new IOException(e);
            }
            writeListMap(logId, LogType.RESPONSE_HEADERS, logEntry.getResponseHeaders());
            writeCookies(logId, LogType.RESPONSE_COOKIES, logEntry.getResponseCookies());
            write(logId, LogType.RESPONSE_CONTENT_LENGTH, logEntry.getResponseContentLength(), false);
            write(logId, LogType.RESPONSE_CHARACTER_ENCODING, logEntry.getResponseCharacterEncoding(), false);
            write(logId, LogType.RESPONSE_CONTENT_TYPE, logEntry.getResponseContentType(), false);
            write(logId, LogType.RESPONSE_LOCALE, logEntry.getResponseLocale(), false);
            write(logId, LogType.STATUS_CODE, logEntry.getStatusCode(), false);
            write(logId, LogType.STATUS_MESSAGE, logEntry.getStatusMessage(), true);
            this.logger.flush();
            write(logId, LogType.RESPONSE_BODY, logEntry.getResponseBody(), true);
            this.logger.flush();
            write(logId, LogType.EXCEPTION, logEntry.getException(), true);
            this.logger.flush();
            write(logId, LogType.ELAPSED_TIME, logEntry.getElapsedTime(), false);
            write(logId, LogType.RESULT, logEntry.getResult(), false);
            this.logger.flush();
            String log = new StringBuilder().append(logId).append(this.csvStrategy.getDelimiter()).append(LogType.END).append(this.csvStrategy.getDelimiter()).append(END_OF_LINE).toString();
            this.logger.put(log);
            this.logger.flush();
        }
    }
