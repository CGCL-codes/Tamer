    @SuppressWarnings("unchecked")
    public static void main(String... strings) throws Exception {
        System.setProperty("app.name", "ironrhino");
        System.setProperty("ironrhino.home", System.getProperty("user.home") + "/" + System.getProperty("app.name"));
        System.setProperty("ironrhino.context", System.getProperty("user.home") + "/" + System.getProperty("app.name"));
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "initdata/applicationContext-initdata.xml", "resources/spring/applicationContext-ds.xml", "resources/spring/applicationContext-hibernate.xml" });
        EntityManager<Persistable> entityManager = (EntityManager<Persistable>) ctx.getBean("entityManager");
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(CodecUtils.digest("password"));
        admin.setEnabled(true);
        admin.getRoles().add(UserRole.ROLE_ADMINISTRATOR);
        entityManager.save(admin);
        ctx.close();
    }
