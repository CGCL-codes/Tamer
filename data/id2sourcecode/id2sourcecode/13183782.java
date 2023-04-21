    public void MEM() throws IrregularStringOfBitsException, MemoryElementNotFoundException {
        edumips64.Main.logger.log("SYSCALL (" + this.hashCode() + ") -> MEM");
        if (syscall_n == 1) {
            String filename = fetchString(address);
            int flags_address = (int) address + filename.length();
            flags_address += 8 - (flags_address % 8);
            MemoryElement flags_m = Memory.getInstance().getCell((int) flags_address);
            int flags = (int) flags_m.getValue();
            for (int i = (int) address; i <= flags_address; i += 8) din.Load(Converter.binToHex(Converter.positiveIntToBin(64, i)), 8);
            edumips64.Main.logger.debug("We must open " + filename + " with flags " + flags);
            return_value = -1;
            try {
                return_value = edumips64.Main.iom.open(filename, flags);
            } catch (java.io.FileNotFoundException e) {
                JOptionPane.showMessageDialog(edumips64.Main.ioFrame, CurrentLocale.getString("FILE_NOT_FOUND"), "EduMIPS64 - " + CurrentLocale.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
            } catch (IOManagerException e) {
                JOptionPane.showMessageDialog(edumips64.Main.ioFrame, CurrentLocale.getString(e.getMessage()), "EduMIPS64 - " + CurrentLocale.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(edumips64.Main.ioFrame, CurrentLocale.getString("IOEXCEPTION"), "EduMIPS64 - " + CurrentLocale.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
            }
        } else if (syscall_n == 2) {
            MemoryElement fd_cell = Memory.getInstance().getCell((int) address);
            int fd = (int) fd_cell.getValue();
            edumips64.Main.logger.debug("Closing fd " + fd);
            return_value = -1;
            try {
                return_value = edumips64.Main.iom.close(fd);
            } catch (IOException e1) {
                edumips64.Main.logger.debug("Error in closing " + fd);
            }
        } else if ((syscall_n == 3) || (syscall_n == 4)) {
            int fd, count;
            long buf_addr;
            MemoryElement temp = Memory.getInstance().getCell((int) address);
            fd = (int) temp.getValue();
            address += 8;
            temp = Memory.getInstance().getCell((int) address);
            buf_addr = temp.getValue();
            address += 8;
            temp = Memory.getInstance().getCell((int) address);
            count = (int) temp.getValue();
            address += 8;
            return_value = -1;
            try {
                if (syscall_n == 3) {
                    edumips64.Main.logger.debug("SYSCALL (" + this.hashCode() + "): trying to read from fd " + fd + " " + count + " bytes, writing them to address " + buf_addr);
                    return_value = edumips64.Main.iom.read(fd, buf_addr, count);
                } else {
                    edumips64.Main.logger.debug("SYSCALL (" + this.hashCode() + "): trying to write to fd " + fd + " " + count + " bytes, reading them from address " + buf_addr);
                    return_value = edumips64.Main.iom.write(fd, buf_addr, count);
                }
            } catch (java.io.FileNotFoundException e) {
                JOptionPane.showMessageDialog(edumips64.Main.ioFrame, CurrentLocale.getString("FILE_NOT_FOUND"), "EduMIPS64 - " + CurrentLocale.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
            } catch (IOManagerException e) {
                JOptionPane.showMessageDialog(edumips64.Main.ioFrame, CurrentLocale.getString(e.getMessage()), "EduMIPS64 - " + CurrentLocale.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(edumips64.Main.ioFrame, CurrentLocale.getString("IOEXCEPTION"), "EduMIPS64 - " + CurrentLocale.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
            }
        } else if (syscall_n == 5) {
            StringBuffer temp = new StringBuffer();
            edumips64.Main.logger.debug("Reading memory cell at address " + address + ", searching for the address of the format string");
            MemoryElement tempMemCell = memory.getCell((int) address);
            int format_string_address = (int) tempMemCell.getValue();
            din.Load(Converter.binToHex(Converter.positiveIntToBin(64, address)), 8);
            String format_string = fetchString(format_string_address);
            edumips64.Main.logger.debug("Read " + format_string);
            int next_param_address = (int) address + 8;
            int t1 = (int) format_string_address + format_string.length();
            t1 += 8 - (t1 % 8);
            for (int i = (int) format_string_address; i < t1; i += 8) din.Load(Converter.binToHex(Converter.positiveIntToBin(64, i)), 8);
            int oldIndex = 0, newIndex = 0;
            while ((newIndex = format_string.indexOf('%', oldIndex)) > 0) {
                char type = format_string.charAt(newIndex + 1);
                temp.append(format_string.substring(oldIndex, newIndex));
                switch(type) {
                    case 's':
                        tempMemCell = memory.getCell(next_param_address);
                        int str_address = (int) tempMemCell.getValue();
                        edumips64.Main.logger.debug("Retrieving the string @ " + str_address + "...");
                        String param = fetchString(str_address);
                        next_param_address += 8;
                        int t2 = str_address + param.length();
                        t2 += 8 - (t2 % 8);
                        for (int i = str_address; i < t2; i += 8) din.Load(Converter.binToHex(Converter.positiveIntToBin(64, i)), 8);
                        edumips64.Main.logger.debug("Got " + param);
                        temp.append(param);
                        break;
                    case 'i':
                    case 'd':
                        edumips64.Main.logger.debug("Retrieving the integer @ " + next_param_address + "...");
                        MemoryElement memCell = memory.getCell((int) next_param_address);
                        din.Load(Converter.binToHex(Converter.positiveIntToBin(64, next_param_address)), 8);
                        Long val = memCell.getValue();
                        next_param_address += 8;
                        temp.append(val.toString());
                        edumips64.Main.logger.debug("Got " + val);
                        break;
                    case '%':
                        edumips64.Main.logger.debug("Literal %...");
                        temp.append('%');
                        break;
                    default:
                        edumips64.Main.logger.debug("Unknown placeholder");
                        break;
                }
                oldIndex = newIndex + 2;
            }
            temp.append(format_string.substring(oldIndex));
            edumips64.Main.logger.debug("That became " + temp.toString());
            edumips64.Main.ioFrame.write(temp.toString());
            return_value = temp.length();
        }
    }
