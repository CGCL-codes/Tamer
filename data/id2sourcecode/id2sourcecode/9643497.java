    public void doDownload(JSONObject event, OutputStream stream) throws IOException {
        if ("exposedFile".equals(event.get("method"))) {
            String fileName = (String) event.get("file");
            java.io.File file = new java.io.File(theSession.getApp().getEnvironment().getLogger().getExposedDir() + fileName);
            String cp;
            try {
                cp = file.getCanonicalPath();
            } catch (IOException e) {
                cp = null;
            }
            if (cp == null || file.isHidden() || file.getName().startsWith(".") || !cp.startsWith(theSession.getApp().getEnvironment().getLogger().getExposedDir())) {
                log.error("The file referred to (" + fileName + ") does not exist or is not exposed");
                return;
            }
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            int read = fis.read();
            while (read >= 0) {
                stream.write(read);
                read = fis.read();
            }
            fis.close();
            stream.close();
            return;
        } else {
            try {
                int[] ids;
                synchronized (theSnapshot) {
                    ids = theSnapshot.toArray();
                }
                Settings settings = new Settings();
                settings.fromJson((JSONObject) event.get("settings"));
                prisms.logging.PrismsLogger logger = theSession.getApp().getEnvironment().getLogger();
                java.io.PrintWriter writer = new java.io.PrintWriter(stream);
                String format = (String) event.get("format");
                String title = "Logs";
                try {
                    title += " on " + new java.net.URL(theSession.getApp().getEnvironment().getIDs().getLocalInstance().location).getHost();
                } catch (Exception e) {
                }
                title += " for search \"";
                if (theSession.getProperty(log4j.app.Log4jProperties.search) == null) title += "*"; else title += theSession.getProperty(log4j.app.Log4jProperties.search);
                title += "\" on " + prisms.util.PrismsUtils.print(theLastCheckTime);
                if ("text/plain".equals(format)) writer.write(title + "\n\n"); else if ("text/rtf".equals(format)) {
                    writer.write("{\\rtf\\ansi\\deff0{\\fonttbl{\\f0\\fswiss\\fcharset0 Arial;}}\n");
                    writer.write("{\\colortbl ");
                    writer.write(";\\red0\\green0\\blue0");
                    writer.write(";\\red255\\green0\\blue0");
                    writer.write(";\\red192\\green192\\blue0");
                    writer.write(";\\red0\\green0\\blue255");
                    writer.write(";\\red0\\green208\\blue0;}\n");
                    writer.write("{\\*\\generator PRISMS Logging " + theSession.getApp().getVersionString() + ";}");
                    writer.write("\\viewkind4\\uc1\\pard\n");
                    writer.write("\\cf1\\f0\\fs32");
                    writer.write(title);
                    writer.write("\\par\\fs20\n\\par\n");
                } else writer.write("<html><body>\n<h1>" + title + "</h1>\n<br />\n");
                thePI.setProgressScale(ids.length);
                thePI.setProgressText("Retrieving and writing log entries");
                long[] tempIDs = new long[ids.length <= 100 ? ids.length : 100];
                for (int i = 0; i < ids.length; i += tempIDs.length) {
                    thePI.setProgress(i);
                    if (tempIDs.length > ids.length - i) tempIDs = new long[ids.length - i];
                    for (int j = 0; j < tempIDs.length; j++) tempIDs[j] = ids[i + j];
                    LogEntry[] entries = logger.getItems(tempIDs);
                    for (int j = 0; j < entries.length; j++) {
                        thePI.setProgress(i + j);
                        settings.print(entries[j], writer, format);
                    }
                }
                if ("text/plain".equals(format)) writer.write("\n"); else if ("text/rtf".equals(format)) writer.write("\\par\n}"); else writer.write("</body></html>\n");
                writer.close();
                stream.close();
            } finally {
                thePI.setDone();
                thePI = null;
            }
        }
    }
