    void measopsButtonDown_actionPerformed(ActionEvent e) {
        int index = m_measurementsList.getSelectedRow();
        Measurement[] m = m_project.getModel().getMeasurement();
        Measurement temp = m[index];
        m[index] = m[index + 1];
        m[index + 1] = temp;
        m_project.getModel().setMeasurement(m);
        m_project.setModified();
        Analysis a = m_project.removeAnalysis(index);
        m_project.insertAnalysis(a, index + 1);
        setSelected(index + 1);
    }
