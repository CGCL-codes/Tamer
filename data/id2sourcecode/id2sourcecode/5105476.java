            public void actionPerformed(ActionEvent e) {
                int i = problemsList.getSelectedIndex();
                if (i >= problemsListModel.size() - 1 || i == -1) return;
                ProblemsBean pb1 = (ProblemsBean) problemsListModel.getElementAt(i);
                ProblemsBean pb2 = (ProblemsBean) problemsListModel.getElementAt(i + 1);
                problemsListModel.setElementAt(pb1, i + 1);
                problemsListModel.setElementAt(pb2, i);
                ProblemDescription[] problemDescriptions = updatedBean.getProblemDescriptions();
                ProblemDescription temp = problemDescriptions[i];
                problemDescriptions[i] = problemDescriptions[i + 1];
                problemDescriptions[i + 1] = temp;
            }
