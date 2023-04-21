    private synchronized void updateIcals(EntityManager em) {
        Query query = em.createNamedQuery("EventICals");
        List<AssociationEntity> list = query.getResultList();
        CalendarBuilder builder = new CalendarBuilder();
        String icalURL = "";
        for (int i = 0; i < list.size(); i++) {
            try {
                AssociationEntity entity = list.get(i);
                icalURL = entity.getIcalURL();
                URL url = new URL(icalURL);
                URLConnection con = url.openConnection();
                net.fortuna.ical4j.model.Calendar calendar = builder.build(con.getInputStream());
                List events = calendar.getComponents(Component.VEVENT);
                for (int j = 0; j < events.size(); j++) {
                    EventBO event = new EventBO((VEvent) events.get(j), em, entity.getIsoCountryCode());
                    event.updateCoordinnates();
                    em.persist(event.getEntity());
                    log.info("Updated event from ICal: " + event.getTitle());
                }
            } catch (Exception e) {
                log.error("Error updateIcals: " + e.getMessage() + " iCal URL: " + icalURL, e);
            }
        }
    }
