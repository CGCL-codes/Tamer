    public Item doModify(String sessionId, String itemId, String parentId, String status, String type, String subject, String sender, String recipient, Date startDate, Date deadline, String task, Date negotiatedDeadline, String reasonForNegotiation, String reasonForRejection, String feedback, String commentsOnFeedback, String priority, boolean archived, boolean reference, String acceptNegoReject, String confirmedUnconfirmed, String links, String tags, FileRepository fileRepository) throws UnsupportedEncodingException, IOException {
        sessionId = (String) RuntimeAccess.getInstance().getSession().getAttribute("SESSION_ID");
        log(INFO, "doModify Session id=" + sessionId);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        UpdateItemRequest request = new UpdateItemRequest();
        log(INFO, "one");
        Item item = new Item();
        item.setArchived(archived);
        item.setCommentsOnFeedback(commentsOnFeedback);
        item.setDeadLine(deadline);
        item.setFeedback(feedback);
        item.setId(itemId);
        item.setNegotiatedDeadLine(negotiatedDeadline);
        item.setReasonForNegotiatiationOfDeadLine(reasonForNegotiation);
        item.setReasonForRejectionOfTask(reasonForRejection);
        item.setStartDate(startDate);
        item.setTask(task);
        item.setPriority(Priority.valueOf(priority));
        item.setSubject(subject);
        item.setType(type);
        item.setSender(sender);
        item.setParentId(parentId);
        item.setFileRepository(fileRepository);
        String[] recip = recipient.split(",");
        item.setRecipient(recip[0]);
        Set<String> recipSet = new HashSet<String>();
        recipSet.addAll(Arrays.asList(recip));
        item.setRecipients(recipSet);
        if (links == null || ("").equals(links.trim())) {
        } else {
            String[] alinks = links.split(",");
            Set<String> linksSet = new HashSet<String>();
            linksSet.addAll(Arrays.asList(alinks));
            item.setLinks(linksSet);
        }
        if (tags == null || ("").equals(tags.trim())) {
        } else {
            String[] atags = tags.split(",");
            Set<String> tagsSet = new HashSet<String>();
            tagsSet.addAll(Arrays.asList(atags));
            item.setTags(tagsSet);
        }
        if (status != null) {
            item.setStatus(Item.Status.valueOf(status));
        } else {
            item.setStatus(null);
        }
        Map<String, String> parameters = new HashMap<String, String>();
        log(INFO, "two");
        parameters.put("CONFIRM_STATE", "Approved".equals(confirmedUnconfirmed) ? "CONFIRMED" : "NOT_CONFIRMED");
        parameters.put("ACCEPT_STATE", "Accept".equals(acceptNegoReject) ? "ACCEPT" : "Negotiate".equals(acceptNegoReject) ? "NEGOTIATE" : "REJECT");
        request.setNewItem(item);
        request.setParameters(parameters);
        request.setSessionId(sessionId);
        log(INFO, "three");
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("UpdateItemRequest", UpdateItemRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("UpdateItemResponse", UpdateItemResponse.class);
        log(INFO, "four");
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/resources/updateItem?REQUEST=" + strRequest);
        log(INFO, "five");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            log(INFO, "Result:" + result);
            UpdateItemResponse oResponse = (UpdateItemResponse) reader.fromXML(result);
            return oResponse.getUpdatedItem();
        }
        return null;
    }
