    public static void main(String[] args) {
        BeanFactory factory = new XmlBeanFactory(new FileSystemResource("WebRoot/WEB-INF/classes/com/lullabysoft/demo/spring/beans.xml"));
        MessageDigester digester = (MessageDigester) factory.getBean("digester");
        digester.digest("Hello World!");
    }
