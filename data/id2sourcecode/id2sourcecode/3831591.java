    public int setContact(Contact contact) {
        if (!contact.jid.equals("")) {
            if (contact.name == null && (contact.name = (String) names.get(contact.jid)) == null) contact.name = ""; else names.put(contact.jid, contact.name);
            if (contact.show != Contact.none) shows.put(contact.jid, new Integer(contact.show)); else if (shows.containsKey(contact.jid)) contact.show = ((Integer) shows.get(contact.jid)).intValue();
            if (contact.status != null) statuses.put(contact.jid, contact.status); else if (statuses.containsKey(contact.jid)) contact.status = (String) statuses.get(contact.jid);
            if (!contact.group.equals("")) {
                if (!groups.containsKey(contact.jid)) groups.put(contact.jid, new Vector());
                if (((Vector) groups.get(contact.jid)).indexOf(contact.group) == -1) ((Vector) groups.get(contact.jid)).addElement(contact.group);
                if (!presentGroups.containsKey(contact.group)) {
                    presentGroups.put(contact.group, new Object());
                    setContact(new Contact(contact.group));
                }
            }
        }
        int b = 0;
        if (!contacts.isEmpty()) {
            int a = contacts.size();
            while (a > b) {
                int cen = (b + a) / 2;
                Contact con = (Contact) contacts.elementAt(cen);
                int cmp = contact.compare(con);
                if (cmp == 0) {
                    contacts.setElementAt(contact, cen);
                    set(cen, (contact.name.equals("") ? contact.jid : contact.name) + ((contact.status == null || contact.status.equals("")) ? "" : (" : " + contact.status)), images[contact.show]);
                    return cen;
                } else if (cmp > 0) b = cen + 1; else a = cen;
            }
        }
        contacts.insertElementAt(contact, b);
        insert(b, (contact.name.equals("") ? contact.jid : contact.name) + ((contact.status == null || contact.status.equals("")) ? "" : (" : " + contact.status)), images[contact.show]);
        return b;
    }
