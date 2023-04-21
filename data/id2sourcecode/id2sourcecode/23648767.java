    private void writeSummary(String thread, String label, Date startTime, String resultFileName, String messages) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String time = sdf.format(startTime);
            String htmlBasePath = getHtmlBasePath() + java.io.File.separator;
            String fqSummaryFileName = htmlBasePath + resultFileName;
            FileWriter summaryWriter = new FileWriter(fqSummaryFileName);
            summaryWriter.write("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'><html><head>");
            summaryWriter.write("<title>JMeter-Test: " + label + " at " + time + "</title>");
            summaryWriter.write("<meta name='GENERATOR' content='snmpJMeter'><meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head><body>");
            summaryWriter.write("<p>");
            summaryWriter.write("<table>");
            summaryWriter.write("<tr><td>Test:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><b>" + label + "</b></td></tr>");
            summaryWriter.write("<tr><td>Time:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><b>" + time + "</b></td></tr>");
            summaryWriter.write("</table>");
            summaryWriter.write("</p>");
            summaryWriter.write("<table><thead><tr><th>Assertion</th><th>Message</th></tr></thead><tbody>");
            summaryWriter.write(messages);
            summaryWriter.write("</tbody></table>");
            summaryWriter.write("<iframe height='700px' width='100%' src='" + thread + "/" + resultFileName + "'></iframe>");
            summaryWriter.write("</body></html>");
            summaryWriter.flush();
            summaryWriter.close();
            Debug.debug("Wrote " + fqSummaryFileName);
        } catch (Exception e) {
            log.error("Could open summary file", e);
        }
    }
