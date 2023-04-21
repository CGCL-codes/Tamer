    public void startProduction(ImageConsumer ic) {
        if (!isConsumer(ic)) addConsumer(ic);
        Vector list = (Vector) consumers.clone();
        try {
            if (input == null) {
                try {
                    if (url != null) input = url.openStream(); else if (datainput != null) input = new DataInputStreamWrapper(datainput); else {
                        if (filename != null) input = new FileInputStream(filename); else input = new ByteArrayInputStream(data, offset, length);
                    }
                    produce(list, input);
                } finally {
                    input = null;
                }
            } else {
                produce(list, input);
            }
        } catch (Exception e) {
            for (int i = 0; i < list.size(); i++) {
                ImageConsumer ic2 = (ImageConsumer) list.elementAt(i);
                ic2.imageComplete(ImageConsumer.IMAGEERROR);
            }
        }
    }
