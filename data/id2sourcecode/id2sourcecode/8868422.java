    public void run() {
        try {
            this.eventsection = new SectionEvent(rq);
            ObjectEvent eventobject;
            if (settings.getPlace().length() < 1) eventobject = eventsection.Methode_Geteventlist(date, settings.getPage(), settings.getLimit(), settings.getRelevance(), settings.getChannelid(), settings.getOrder(), settings.getOrderdirection(), settings.getLoadflyers()); else eventobject = eventsection.Methode_Geteventlist(date, settings.getPage(), settings.getLimit(), settings.getRelevance(), settings.getChannelid(), settings.getOrder(), settings.getOrderdirection(), settings.getLoadflyers(), settings.getPlace(), settings.getRadius());
            ObjectEvent eventobject2 = eventsection.Methode_Geteventcategories("de_AT");
            eventvektor = eventobject.getVector_event();
            for (int i = 0; i < eventvektor.size(); i++) {
                Event event = (Event) eventvektor.elementAt(i);
                String category = event.getCategoryid();
                category = eventobject2.getCategoryName(category);
                EventItem eventitem = new EventItem(event.getName(), event.getLocation(), event.getCity(), category);
                eventitem.addCommand(this.Cmd_Details);
                this.eventlist.append(eventitem);
            }
            if (!isCancel()) this.client.setCurrent(eventlist);
        } catch (ErrorcodeException e) {
            if (!isCancel()) {
                this.client.setCurrent(eventlist);
                AlertError(e.getErrormassage());
            }
        }
    }
