    public void handle(final EnterChannelResponse response) {
        final ChannelDialog c = ChannelDialog.getChannelDialog();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                switch(response.code) {
                    case Code.OK:
                        c.reset();
                        c.setFrozen(false);
                        c.setVisible(false);
                        int state = Window.getWindow().getState();
                        int role = State.getRole(response.state);
                        switch(role) {
                            case State.STUDENT:
                                state = State.setRole(state, State.STUDENT);
                                break;
                            case State.TEACHER:
                                state = State.setRole(state, State.TEACHER);
                                break;
                            case State.MODERATOR:
                                state = State.setRole(state, State.MODERATOR);
                                break;
                        }
                        state = State.set(state, State.IN_A_CHANNEL, true);
                        state = State.set(state, State.AVAILABLE_OR_AWAY, true);
                        Window.getWindow().displayName = response.displayName;
                        Window.getWindow().setState(state, State.IN_A_CHANNEL);
                        break;
                    case Code.NOT_FOUND:
                        c.setStatusText(getString(c, "NOT_FOUND"));
                        c.setFrozen(false);
                        Window.getWindow().channel = null;
                        break;
                    case Code.DENIED:
                        c.setStatusText(getString(c, "DENIED"));
                        c.setFrozen(false);
                        Window.getWindow().channel = null;
                        break;
                    case Code.UNKNOWN:
                        c.setStatusText(getString(c, "UNKNOWN"));
                        c.setFrozen(false);
                        Window.getWindow().channel = null;
                        break;
                }
                if (response.ids != null && response.ids.length > 0) {
                    Classroom cr = Classroom.getClassroom();
                    for (int i = 0; i < response.ids.length; i++) {
                        int id = response.ids[i];
                        String email = response.emails[i];
                        String displayName = response.displayNames[i];
                        int state = response.states[i];
                        Classmate cm = new Classmate();
                        cm.setId(id);
                        cm.setEmail(email);
                        cm.setDisplayName(displayName);
                        cm.setState(state);
                        cr.getClassmateModel().add(cm);
                        reconnect(cm);
                    }
                    cr.updateTableUI();
                }
                Elements elements = Elements.getElements();
                ElementsTreeModel model = elements.getModel();
                Whiteboard w = Whiteboard.getWhiteboard();
                if (response.shapes != null && response.shapes.length > 0) {
                    Shape[] array = convert(response.shapes);
                    for (Shape item : array) {
                        model.add(item);
                    }
                }
                elements.updateTreeUI();
                w.repaint();
            }
        });
    }
