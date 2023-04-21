        @Override
        public String processMacro(Macro macro) {
            if ("include".equals(macro.getId())) {
                String fileName = macro.getParams()[0];
                try {
                    URL url = new URL(ivSource.getURL(), fileName);
                    Reader is = new BufferedReader(new InputStreamReader(url.openStream()));
                    String res = JSDummyParser.parse(is, ivMacroProcessor, ivPreserveComments, JSLOGGER.isDebugEnabled());
                    is.close();
                    return res;
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else if ("logLevel".equals(macro.getId())) {
                return JSLOGGER.isDebugEnabled() ? "7" : JSLOGGER.isInfoEnabled() ? "6" : JSLOGGER.isWarnEnabled() ? "4" : JSLOGGER.isErrorEnabled() ? "3" : "0";
            } else if ("contextPath".equals(macro.getId())) {
                return "'" + ivContextPath + "'";
            } else if ("initParameter".equals(macro.getId())) {
                String propName = macro.getParams()[0];
                String propValue = (propName == null) ? null : getServletConfig().getInitParameter(propName);
                return propValue == null ? "null" : "'" + propValue + "'";
            }
            return null;
        }
