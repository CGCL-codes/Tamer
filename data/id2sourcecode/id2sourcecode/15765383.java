        public Node call() throws Exception {
            if (converter == null) return null;
            URL url = getURL();
            for (int i = 0; i < props.length; i++) converter.setProperty(props[i], url);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            converter.convert(url.openStream(), bos);
            Savable savable = BinaryImporter.getInstance().load(new ByteArrayInputStream(bos.toByteArray()));
            if (savable instanceof Node) {
                return (Node) savable;
            } else {
                Node model = new Node("Imported Model " + file);
                model.attachChild((Spatial) savable);
                return model;
            }
        }
