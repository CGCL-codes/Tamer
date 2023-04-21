    @Test
    public void should_print_file_already_exists_message_when_demanded_to_use_filename_that_exists() {
        final String new_file_name = "baz";
        context.checking(new Expectations() {

            {
                ignoring(mockInterpretable).getArguments();
                will(returnValue(new String[] { new_file_name }));
                one(mockAccessor).writeLine("touch: " + new_file_name + ": File already exists");
            }
        });
        Touch touchCommand = new Touch();
        assertEquals(-1, touchCommand.execute(mockInterpretable));
    }
