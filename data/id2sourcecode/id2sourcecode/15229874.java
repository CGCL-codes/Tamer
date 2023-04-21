    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Tutorial");
        System.out.println("DataNucleus Tutorial with JPA");
        System.out.println("=============================");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Inventory inv = new Inventory("My Inventory");
            Product product = new Product("Sony Discman", "A standard discman from Sony", 200.00);
            inv.getProducts().add(product);
            Book book = new Book("Lord of the Rings by Tolkien", "The classic story", 49.99, "JRR Tolkien", "12345678", "MyBooks Factory");
            inv.getProducts().add(book);
            em.persist(inv);
            tx.commit();
            System.out.println("Product and Book have been persisted");
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            em.close();
        }
        System.out.println("");
        em = emf.createEntityManager();
        tx = em.getTransaction();
        try {
            tx.begin();
            System.out.println("Executing Query for Products with price below 150.00");
            Query q = em.createQuery("SELECT p FROM Product p WHERE p.price < 150.00 ORDER BY p.price");
            List results = q.getResultList();
            Iterator iter = results.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                System.out.println(">  " + obj);
                if (obj instanceof Book) {
                    Book b = (Book) obj;
                    b.setDescription(b.getDescription() + " REDUCED");
                }
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            em.close();
        }
        System.out.println("");
        em = emf.createEntityManager();
        tx = em.getTransaction();
        try {
            tx.begin();
            System.out.println("Deleting all products from persistence");
            Inventory inv = (Inventory) em.find(Inventory.class, "My Inventory");
            System.out.println("Clearing out Inventory");
            inv.getProducts().clear();
            System.out.println("Deleting Inventory");
            em.remove(inv);
            System.out.println("Deleting all products from persistence");
            Query q = em.createQuery("DELETE FROM Product p");
            int numberInstancesDeleted = q.executeUpdate();
            System.out.println("Deleted " + numberInstancesDeleted + " products");
            tx.commit();
        } catch (Exception e) {
            System.out.println("Bulk delete encountered an error " + e.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            em.close();
        }
        System.out.println("");
        System.out.println("End of Tutorial");
    }
