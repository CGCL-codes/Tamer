    public void writeVersions(Date[] versions) throws IOException {
        indent(3);
        if (versions == null) {
            m_writer.println("Datastream " + m_curdsID + " not found in this object");
            m_datastreamCountThisObject = 0;
        } else {
            if (versions.length == 0) {
                m_writer.println("Datastream already has desired ControlGroup)");
            } else {
                for (Date version : versions) {
                    indent(4);
                    m_writer.println("Updated version " + version.toString());
                }
            }
            m_datastreamCountThisObject = versions.length;
        }
    }
