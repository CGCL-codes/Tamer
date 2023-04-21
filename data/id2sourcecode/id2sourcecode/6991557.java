    private void addSchedule2Doc(XmlDoc doc, Element parent, ScheduleItem item, boolean addLog) throws Exception {
        Element elm = null;
        parent.setAttribute("id", item.toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(item.getStart());
        elm = doc.createElement("times");
        Element time = doc.createElement("start");
        time.setAttribute("year", new Integer(cal.get(Calendar.YEAR)).toString());
        time.setAttribute("month", new Integer(cal.get(Calendar.MONTH) + 1).toString());
        time.setAttribute("day", new Integer(cal.get(Calendar.DATE)).toString());
        time.setAttribute("hour", new Integer(cal.get(Calendar.HOUR_OF_DAY)).toString());
        time.setAttribute("minute", new Integer(cal.get(Calendar.MINUTE)).toString());
        elm.appendChild(time);
        cal.setTime(item.getStop());
        time = doc.createElement("stop");
        time.setAttribute("year", new Integer(cal.get(Calendar.YEAR)).toString());
        time.setAttribute("month", new Integer(cal.get(Calendar.MONTH) + 1).toString());
        time.setAttribute("day", new Integer(cal.get(Calendar.DATE)).toString());
        time.setAttribute("hour", new Integer(cal.get(Calendar.HOUR_OF_DAY)).toString());
        time.setAttribute("minute", new Integer(cal.get(Calendar.MINUTE)).toString());
        elm.appendChild(time);
        parent.appendChild(elm);
        elm = doc.createTextElement("name", item.getName());
        parent.appendChild(elm);
        elm = doc.createTextElement("duration", new Integer(item.getDuration()).toString());
        parent.appendChild(elm);
        elm = doc.createTextElement("channel", item.getChannel());
        parent.appendChild(elm);
        elm = doc.createTextElement("state", new Integer(item.getState()).toString());
        parent.appendChild(elm);
        elm = doc.createTextElement("status", item.getStatus());
        parent.appendChild(elm);
        elm = doc.createTextElement("type", new Integer(item.getType()).toString());
        parent.appendChild(elm);
        elm = doc.createTextElement("path", new Integer(item.getCapturePathIndex()).toString());
        parent.appendChild(elm);
        elm = doc.createTextElement("filename_pattern", item.getFilePattern());
        parent.appendChild(elm);
        elm = doc.createTextElement("capture_type", new Integer(item.getCapType()).toString());
        parent.appendChild(elm);
        elm = doc.createTextElement("post_task", item.getPostTask());
        parent.appendChild(elm);
        if (item.isAutoDeletable()) elm = doc.createTextElement("auto_delete", "1"); else elm = doc.createTextElement("auto_delete", "0");
        parent.appendChild(elm);
        elm = doc.createTextElement("keep_for", new Integer(item.getKeepFor()).toString());
        parent.appendChild(elm);
        elm = doc.createElement("warnings");
        Vector<String> warnings = item.getWarnings();
        for (int x = 0; x < warnings.size(); x++) {
            Element warningElm = doc.createTextElement("warning", warnings.get(x));
            elm.appendChild(warningElm);
        }
        parent.appendChild(elm);
        if (addLog) {
            elm = doc.createTextElement("log_data", item.getLog());
            parent.appendChild(elm);
        }
    }
