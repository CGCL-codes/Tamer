    public void testLocalSessionFactoryBeanWithCacheStrategies() throws Exception {
        final Properties registeredClassCache = new Properties();
        final Properties registeredCollectionCache = new Properties();
        LocalSessionFactoryBean sfb = new LocalSessionFactoryBean() {

            protected Configuration newConfiguration() {
                return new Configuration() {

                    public Configuration setCacheConcurrencyStrategy(String clazz, String concurrencyStrategy) {
                        registeredClassCache.setProperty(clazz, concurrencyStrategy);
                        return this;
                    }

                    public Configuration setCollectionCacheConcurrencyStrategy(String collectionRole, String concurrencyStrategy) {
                        registeredCollectionCache.setProperty(collectionRole, concurrencyStrategy);
                        return this;
                    }
                };
            }

            protected SessionFactory newSessionFactory(Configuration config) {
                return null;
            }
        };
        sfb.setMappingResources(new String[0]);
        sfb.setDataSource(new DriverManagerDataSource());
        Properties classCache = new Properties();
        classCache.setProperty("org.springframework.beans.TestBean", "read-write");
        sfb.setEntityCacheStrategies(classCache);
        Properties collectionCache = new Properties();
        collectionCache.setProperty("org.springframework.beans.TestBean.friends", "read-only");
        sfb.setCollectionCacheStrategies(collectionCache);
        sfb.afterPropertiesSet();
        assertEquals(classCache, registeredClassCache);
        assertEquals(collectionCache, registeredCollectionCache);
    }
