    private static void buildIndex(URL url) throws Exception {
        Scanner scanner = new Scanner(url.openStream());
        EntityManager worker = getJPAEntityManagerFactory().createEntityManager();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith("#") || line.isEmpty()) continue;
            EntityTransaction trx = worker.getTransaction();
            trx.begin();
            worker.createNativeQuery(line).executeUpdate();
            trx.commit();
        }
    }
