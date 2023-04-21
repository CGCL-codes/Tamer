    protected void setData() {
        if (contact == null || !isInited()) return;
        int m_temp_gender = 0;
        String m_temp_spouse = "";
        String[] m_temp_children = new String[10];
        if (contact.hasPersonalIdentity()) {
            PersonalIdentity pid = contact.getPersonalIdentity();
            Date birthDate = pid.getBirthDate();
            if (birthDate != null) {
                Calendar birthCal = new GregorianCalendar();
                birthCal.setTime(birthDate);
                birthdayCB.setCalendar(birthCal);
            }
        }
        if (contact.hasExtensions()) {
            m_Extensions = contact.getExtensions();
            m_SimpleExtension_GN = (SimpleExtension) (m_Extensions.get("X-WAB-GENDER"));
            if (m_SimpleExtension_GN != null) {
                try {
                    m_temp_gender = Integer.valueOf(m_SimpleExtension_GN.getValue()).intValue();
                } catch (java.lang.NumberFormatException ne) {
                    m_temp_gender = 0;
                }
            }
            m_SimpleExtension_FM = (SimpleExtension) (m_Extensions.get("X-FAMILY"));
            if (m_SimpleExtension_FM != null) {
                String[] values = m_SimpleExtension_FM.listValues();
                try {
                    m_temp_spouse = values[0];
                    for (int i = 0; i < 10; i++) m_temp_children[i] = values[i + 1];
                } catch (java.lang.IndexOutOfBoundsException ie) {
                }
            }
        }
        if ((m_temp_gender <= 0) && (m_temp_gender >= 2)) m_temp_gender = 0;
        m_cb_gender.setSelectedIndex(m_temp_gender);
        m_f_spouse.setText(m_temp_spouse);
    }
