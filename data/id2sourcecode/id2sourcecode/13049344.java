            private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
                _directory = UtilFiles.addSeparator('.' + getClass().getName());
                _model = (URI) s.readObject();
                _label = (String) s.readObject();
                _learner = (LearnerExternal) s.readObject();
                final String directory = UtilFiles.addSeparator(_directory + _label);
                new File(directory).mkdirs();
                UtilFiles.writeToFile((String) s.readObject(), directory + new File(_model).getName());
            }
