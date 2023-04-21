    @Override
    protected void addDestinations(ConfigurationService config, Writer out) throws Exception {
        StringBuilder secBuf = new StringBuilder();
        if (getRoles() != null && getRoles().length() > 0) {
            secBuf.append("    <attribute name='SecurityConfig'>\n").append("      <security>\n");
            for (String role : getRoles().split(",")) secBuf.append("        <role name='").append(role).append("' read='true' write='true'/>\n");
            secBuf.append("      </security>\n").append("    </attribute>\n");
        }
        StringBuilder mbeanBuf = new StringBuilder();
        String[] queueNames = config.getQueueNames();
        if (queueNames != null) {
            for (String queueName : queueNames) {
                QueueInfo queueInfo = config.getQueueInfo(queueName);
                mbeanBuf.append("  <mbean code='org.jboss.jms.server.destination.QueueService' name='jboss.messaging.destination:service=Queue,name=").append(queueInfo.getName()).append("' xmbean-dd='xmdesc/Queue-xmbean.xml'>\n").append("    <depends optional-attribute-name='ServerPeer'>jboss.messaging:service=ServerPeer</depends>\n").append("    <depends>jboss.messaging:service=PostOffice</depends>\n").append(secBuf.toString()).append("  </mbean>\n");
            }
        }
        String[] topicNames = config.getTopicNames();
        if (topicNames != null) {
            for (String topicName : topicNames) {
                TopicInfo topicInfo = config.getTopicInfo(topicName);
                mbeanBuf.append("  <mbean code='org.jboss.jms.server.destination.TopicService' name='jboss.messaging.destination:service=Topic,name=").append(topicInfo.getName()).append("' xmbean-dd='xmdesc/Queue-xmbean.xml'>\n").append("    <depends optional-attribute-name='ServerPeer'>jboss.messaging:service=ServerPeer</depends>\n").append("    <depends>jboss.messaging:service=PostOffice</depends>\n").append(secBuf.toString()).append("  </mbean>\n");
            }
        }
        StringBuilder buf = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>\n").append(createManifest()).append("<server>\n").append(mbeanBuf.toString()).append("</server>\n");
        out.write(buf.toString());
    }
