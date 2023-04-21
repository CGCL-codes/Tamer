    private void showInternal(final Object o) throws Exception {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();
        f.getContentPane().add(p);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createTitledBorder(o.getClass().getName()));
        List<Method> ms = new ArrayList<Method>();
        for (Method m : o.getClass().getMethods()) {
            ms.add(m);
        }
        Collections.sort(ms, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Method) o1).getName().compareTo(((Method) o2).getName());
            }
        });
        for (final Method m : ms) {
            if (m.getParameterTypes().length != 0) continue;
            if (Modifier.isAbstract(m.getModifiers())) continue;
            if (!m.getDeclaringClass().equals(o.getClass())) continue;
            p.add(new JButton(new AbstractAction(m.getName()) {

                public void actionPerformed(ActionEvent e) {
                    try {
                        System.out.println(m.invoke(o, new Object[0]));
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }));
        }
        f.pack();
        f.setVisible(true);
    }
