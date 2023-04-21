    @Test
    public void should_print_file_already_exists_message_when_demanded_to_use_filename_that_exists() {
        final String new_file_name = "bar";
        context.checking(new Expectations() {

            {
                ignoring(mockInterpretable).getArguments();
                will(returnValue(new String[] { new_file_name }));
                one(mockAccessor).writeLine("mkdir: " + new_file_name + ": Directory already exists");
            }
        });
        Mkdir mkdirCommand = new Mkdir();
        assertEquals(-1, mkdirCommand.execute(mockInterpretable));
    }
