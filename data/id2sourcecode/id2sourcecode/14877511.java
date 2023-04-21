    public static void writeConnectorState(PrintWriter writer, ObjectName tpName, String name, MBeanServer mBeanServer, Vector globalRequestProcessors, Vector requestProcessors, int mode) throws Exception {
        if (mode == 0) {
            writer.print("<h1>");
            writer.print(name);
            writer.print("</h1>");
            writer.print("<p>");
            writer.print(" Max threads: ");
            writer.print(mBeanServer.getAttribute(tpName, "maxThreads"));
            writer.print(" Min spare threads: ");
            writer.print(mBeanServer.getAttribute(tpName, "minSpareThreads"));
            writer.print(" Max spare threads: ");
            writer.print(mBeanServer.getAttribute(tpName, "maxSpareThreads"));
            writer.print(" Current thread count: ");
            writer.print(mBeanServer.getAttribute(tpName, "currentThreadCount"));
            writer.print(" Current thread busy: ");
            writer.print(mBeanServer.getAttribute(tpName, "currentThreadsBusy"));
            try {
                Object value = mBeanServer.getAttribute(tpName, "keepAliveCount");
                writer.print(" Keeped alive sockets count: ");
                writer.print(value);
            } catch (Exception e) {
            }
            writer.print("<br>");
            ObjectName grpName = null;
            Enumeration enumeration = globalRequestProcessors.elements();
            while (enumeration.hasMoreElements()) {
                ObjectName objectName = (ObjectName) enumeration.nextElement();
                if (name.equals(objectName.getKeyProperty("name"))) {
                    grpName = objectName;
                }
            }
            if (grpName == null) {
                return;
            }
            writer.print(" Max processing time: ");
            writer.print(formatTime(mBeanServer.getAttribute(grpName, "maxTime"), false));
            writer.print(" Processing time: ");
            writer.print(formatTime(mBeanServer.getAttribute(grpName, "processingTime"), true));
            writer.print(" Request count: ");
            writer.print(mBeanServer.getAttribute(grpName, "requestCount"));
            writer.print(" Error count: ");
            writer.print(mBeanServer.getAttribute(grpName, "errorCount"));
            writer.print(" Bytes received: ");
            writer.print(formatSize(mBeanServer.getAttribute(grpName, "bytesReceived"), true));
            writer.print(" Bytes sent: ");
            writer.print(formatSize(mBeanServer.getAttribute(grpName, "bytesSent"), true));
            writer.print("</p>");
            writer.print("<table border=\"0\"><tr><th>Stage</th><th>Time</th><th>B Sent</th><th>B Recv</th><th>Client</th><th>VHost</th><th>Request</th></tr>");
            enumeration = requestProcessors.elements();
            while (enumeration.hasMoreElements()) {
                ObjectName objectName = (ObjectName) enumeration.nextElement();
                if (name.equals(objectName.getKeyProperty("worker"))) {
                    writer.print("<tr>");
                    writeProcessorState(writer, objectName, mBeanServer, mode);
                    writer.print("</tr>");
                }
            }
            writer.print("</table>");
            writer.print("<p>");
            writer.print("P: Parse and prepare request S: Service F: Finishing R: Ready K: Keepalive");
            writer.print("</p>");
        } else if (mode == 1) {
            writer.write("<connector name='" + name + "'>");
            writer.write("<threadInfo ");
            writer.write(" maxThreads=\"" + mBeanServer.getAttribute(tpName, "maxThreads") + "\"");
            writer.write(" minSpareThreads=\"" + mBeanServer.getAttribute(tpName, "minSpareThreads") + "\"");
            writer.write(" maxSpareThreads=\"" + mBeanServer.getAttribute(tpName, "maxSpareThreads") + "\"");
            writer.write(" currentThreadCount=\"" + mBeanServer.getAttribute(tpName, "currentThreadCount") + "\"");
            writer.write(" currentThreadsBusy=\"" + mBeanServer.getAttribute(tpName, "currentThreadsBusy") + "\"");
            writer.write(" />");
            ObjectName grpName = null;
            Enumeration enumeration = globalRequestProcessors.elements();
            while (enumeration.hasMoreElements()) {
                ObjectName objectName = (ObjectName) enumeration.nextElement();
                if (name.equals(objectName.getKeyProperty("name"))) {
                    grpName = objectName;
                }
            }
            if (grpName != null) {
                writer.write("<requestInfo ");
                writer.write(" maxTime=\"" + mBeanServer.getAttribute(grpName, "maxTime") + "\"");
                writer.write(" processingTime=\"" + mBeanServer.getAttribute(grpName, "processingTime") + "\"");
                writer.write(" requestCount=\"" + mBeanServer.getAttribute(grpName, "requestCount") + "\"");
                writer.write(" errorCount=\"" + mBeanServer.getAttribute(grpName, "errorCount") + "\"");
                writer.write(" bytesReceived=\"" + mBeanServer.getAttribute(grpName, "bytesReceived") + "\"");
                writer.write(" bytesSent=\"" + mBeanServer.getAttribute(grpName, "bytesSent") + "\"");
                writer.write(" />");
                writer.write("<workers>");
                enumeration = requestProcessors.elements();
                while (enumeration.hasMoreElements()) {
                    ObjectName objectName = (ObjectName) enumeration.nextElement();
                    if (name.equals(objectName.getKeyProperty("worker"))) {
                        writeProcessorState(writer, objectName, mBeanServer, mode);
                    }
                }
                writer.write("</workers>");
            }
            writer.write("</connector>");
        }
    }
