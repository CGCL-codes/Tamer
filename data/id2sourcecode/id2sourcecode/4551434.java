            @Override
            public void mouseClicked(MouseEvent e) {
                paramTextRB.setText(null);
                if (paramVariable != null && paramVariable.getChannelRB() != null) {
                    paramValueRB = paramVariable.getValueRB();
                    paramTextRB.setText(valueFormat.format(paramValueRB));
                }
            }
