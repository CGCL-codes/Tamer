    void digestDelivery(Element root) {
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            Delivery delivery = getDelivery(child.getName());
            if (delivery != null) {
                delivery.digest(child);
                setDelivery(delivery);
                break;
            }
        }
    }
