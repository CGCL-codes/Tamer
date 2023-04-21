    void digestFeedback(Element root) {
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            Feedback feedback = getFeedback(child.getName());
            ;
            if (feedback != null) {
                feedback.digest(child);
                setFeedback(feedback);
            }
        }
    }
