    private boolean contentChanged(final String url, final String content, final MonitoringInfo monitoringInfo) {
        messageDigest.update(content.getBytes());
        final String contentRepresentation = new String(messageDigest.digest()).trim();
        final String storedContentRepresentation = monitoringInfo.getContentRepresentation();
        final boolean contentChanged = !contentRepresentation.equals(storedContentRepresentation);
        if (contentChanged) {
            monitoringInfo.setContentRepresentation(contentRepresentation);
            monitoringInfo.addDateToChangeHistory(new Date());
        }
        return contentChanged;
    }
