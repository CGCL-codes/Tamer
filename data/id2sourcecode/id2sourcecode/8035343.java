        public JMSContext(Hashtable<?, ?> environment) {
            this.environment = environment;
            context.put("ConnectionFactory", new ConnectionFactory() {

                public Connection createConnection() throws JMSException {
                    return createConnection(null, null);
                }

                public Connection createConnection(String string, String string1) throws JMSException {
                    return new JMSConnection(JMSContext.this);
                }
            });
            Object url = environment.get(Context.PROVIDER_URL);
            if (url != null) {
                try {
                    Properties props = new Properties();
                    props.load(new URI(url.toString()).toURL().openStream());
                    for (Object key : props.keySet()) {
                        String name = key.toString();
                        if (name.startsWith("topic.")) {
                            context.put(name.substring(6), new JMSDestination(props.getProperty(name)));
                        } else if (name.startsWith("queue.")) {
                            context.put(name.substring(6), new JMSDestination(props.getProperty(name)));
                        }
                    }
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Trouble reading context environment from URL " + url, e);
                }
            } else {
                for (Object env : environment.keySet()) {
                    String name = env.toString();
                    if (name.startsWith("topic.")) {
                        context.put(name.substring(6), new JMSDestination(environment.get(env).toString()));
                    } else if (name.startsWith("queue.")) {
                        context.put(name.substring(6), new JMSDestination(environment.get(env).toString()));
                    }
                }
            }
        }
