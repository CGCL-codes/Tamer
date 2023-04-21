    private String checkRabbitMQ() {
        StringBuilder sb = new StringBuilder();
        String host = MetadataServerContext.getInstance().getConf().getProperty("URLQueue", "ServerHost", "localhost");
        try {
            RabbitQueue<Message> queue = new RabbitQueue<Message>(host, "testing");
            Channel chnl = queue.getChannel();
            chnl.close();
            sb.append("<h3>RabbitMQ: ").append(host).append(" 状态正常</h3>");
        } catch (Exception ex) {
            ex.printStackTrace();
            sb.append("<h3><font color='red'>RabbitMQ: ").append(host).append(" 状态异常</font></h3>");
        }
        return sb.toString();
    }
