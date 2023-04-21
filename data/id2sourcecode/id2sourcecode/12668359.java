    public void testXML() {
        TestBeanDTD1 sourceBean = (TestBeanDTD1) this.setupComplexTestBean();
        Vector xmlResult = new Vector(4);
        try {
            System.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");
            {
                StringWriter w = new StringWriter(1000);
                (new JOXBeanWriter(w, false)).writeObject("MarkTest", sourceBean);
                xmlResult.add(w);
            }
            {
                StringWriter w = new StringWriter(1000);
                (new JOXBeanWriter(w, true)).writeObject("MarkTest", sourceBean);
                xmlResult.add(w);
            }
            {
                StringWriter w = new StringWriter(1000);
                JOXBeanWriter withDate = new JOXBeanWriter(w, false);
                withDate.setDateFormat(new SimpleDateFormat("HH ss mm dd MM z yyyy"));
                withDate.writeObject("MarkTest", sourceBean);
                xmlResult.add(w);
            }
            {
                StringWriter w = new StringWriter(1000);
                (new JOXBeanWriter(this.readDTD("testDOM3.dtd"), w)).writeObject("MarkTest", sourceBean);
                xmlResult.add(w);
            }
        } catch (IOException e) {
            throw new RuntimeException("while making xmlResults, @" + (xmlResult.size() - 1) + " " + e.getMessage());
        }
        int ct = 0;
        try {
            for (Iterator itor = xmlResult.iterator(); itor.hasNext(); ) {
                String axmlResult = ((StringWriter) itor.next()).toString();
                Object configuredBean = (new JOXBeanReader(new StringReader(axmlResult))).readObject(TestBeanDTD1.class);
                assertEquals("XML roundtrip " + ct, sourceBean, configuredBean);
                ct++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("while processing, @" + ct + " " + e.getMessage() + "\n");
        }
    }
