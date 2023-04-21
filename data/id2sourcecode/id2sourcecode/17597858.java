    private void createEntry(final String name) throws IOException {
        LogUtils.infof(this, "adding to zip: opennms-system-report/%s", name);
        m_zipOutputStream.putNextEntry(new ZipEntry("opennms-system-report/" + name));
    }
