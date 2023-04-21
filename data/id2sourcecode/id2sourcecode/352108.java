    private static void checkProperty(String id, XMLReader producer, Object newValue) {
        int state = 0;
        try {
            Object value;
            final int align = 20;
            for (int i = align - id.length(); i > 0; i--) System.out.print(' ');
            System.out.print(id);
            System.out.print(":  ");
            id = PROPERTY_URI + id;
            value = producer.getProperty(id);
            System.out.print(value);
            System.out.print(", ");
            state = 1;
            producer.setProperty(id, value);
            state = 2;
            producer.setProperty(id, newValue);
            System.out.println("read and write");
        } catch (SAXNotSupportedException e) {
            switch(state) {
                case 0:
                    System.out.println("(can't read now)");
                    break;
                case 1:
                    System.out.println("bogus_1");
                    break;
                case 2:
                    System.out.println("readonly");
                    break;
            }
        } catch (SAXNotRecognizedException e) {
            if (state == 0) System.out.println("(unrecognized)"); else System.out.println("bogus_2");
        }
    }
