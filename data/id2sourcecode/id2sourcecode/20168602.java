	@SuppressWarnings("deprecation")
	public String insertion(){
		try {
			if(	inputvalidate()==false)
				return INPUT;
			else{
				System.out.println("uploadFile execute");
				program.setImage(request.getContextPath()+"/upload/"+filename);
				/* do the upload*/
				String realPath = ServletActionContext.getRequest().getRealPath(root);
				String targetDirectory = realPath;
				targetFileName = filename;
				setDir(targetDirectory + "\\" + targetFileName);
				if(!dir.substring(dir.length()-3,dir.length()).equals("jpg")){
					resultmsg = "file type error, please upload an .jpg image";
					System.out.println(resultmsg);
					return INPUT;
				}
				File target = new File(targetDirectory,targetFileName);
				FileUtils.copyFile(imagefile, target);
			}			
			program.setProgramid(manageProgramService.insertProgram(program));
			programName = "";
			return SUCCESS;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return ERROR;
		}
	}
