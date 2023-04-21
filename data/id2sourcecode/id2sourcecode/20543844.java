    public void processPage() {
        String name = getParameter("name");
        String type = getParameter("type");
        String reference = getParameter("reference");
        String phonenr = getContext().getSessionAttributeAsString(HamboUser.MOBILE_NUMBER);
        StringBuffer cetevo_link = new StringBuffer(RTIApplication.CETEVO_URL + "termconfig/IMAGE_RINGTONE.CGI?");
        String listenlink = RTIApplication.CETEVO_URL + "termconfig/get_ringtone.CGI?index=" + reference;
        String send_msg = "No message";
        boolean send = false;
        boolean error = false;
        boolean isimage = false;
        Element status_msg = getElement("status_msg");
        String ringtone = null;
        boolean cheep = false;
        String category = null;
        if (getParameter("submit") == null) {
            Element element = getElement("type");
            element.setAttribute("value", type);
            element = getElement("phonenr");
            element.setAttribute("value", phonenr);
            element = getElement("namet");
            DOMUtil.setFirstNodeText(element, name);
            element = getElement("listenurl");
            element.setAttribute("value", listenlink);
            element = getElement("listen");
            if (element != null) DOMUtil.appendAttribute(element, "src", reference);
            element = getElement("name");
            element.setAttribute("value", reference);
            status_msg.getParentNode().removeChild(status_msg);
        } else if (getParameter("submit") != null) {
            String image = null;
            String imagetype = null;
            if (getParameter("image") != null) {
                imagetype = getParameter("imagetype");
                image = getParameter("image");
                String operator = "";
                isimage = true;
            } else {
                String device = getParameter("device");
            }
            phonenr = getParameter("phonenr");
            ringtone = getParameter("name");
            if (phonenr != null) {
                if (phonenr.length() < 9) {
                    send_msg = "(@rttofewnumbers@)";
                    error = true;
                }
                if (!phonenr.substring(0, 1).equals("+")) {
                    send_msg = "(@rtsupposedtofillin@)";
                    error = true;
                }
            } else {
                error = true;
                send = false;
                send_msg = "(@rttofewnumbers@)";
            }
            if (ringtone != null) send = true; else {
                error = true;
                send_msg = "(@rtmidisupposedtofillin@)";
            }
            if (!isimage) {
                cetevo_link.append("type").append("=").append("ringtone");
                cetevo_link.append("&").append("ringtone").append("=").append(ringtone);
            } else {
                cetevo_link.append("type").append("=").append("image");
                cetevo_link.append("&").append("imagetype").append("=").append(imagetype);
                cetevo_link.append("&").append("image").append("=").append(image);
                cetevo_link.append("&").append("operator").append("=").append("");
            }
            cetevo_link.append("&").append("device").append("=").append(device);
            cetevo_link.append("&").append("phonenr").append("=").append(phonenr);
            cetevo_link.append("&").append("user_id").append("=").append(user_id);
        }
        if (send && !error) {
            try {
                send_msg = "(@rtringsent@)";
                fireUserEvent("rtSendRintone", new String[][] { { "Number", phonenr }, { "Sourse", "public" }, { "File", ringtone } });
                try {
                    makeCreditTransaction(CreditService.SERVICE_RTI_RINGTONE);
                } catch (Exception ex) {
                }
                URL url = new URL(cetevo_link.toString());
                HttpURLConnection htcon = (HttpURLConnection) url.openConnection();
                int response = htcon.getResponseCode();
            } catch (Exception e) {
                logError("Failed to connect to WTC", e);
                send_msg = "(@rtmidisentfailed@)";
            }
            DOMUtil.setFirstNodeText(status_msg, send_msg);
        }
        DOMUtil.setFirstNodeText(status_msg, send_msg);
    }
