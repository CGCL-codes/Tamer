	@SuppressWarnings("deprecation")
	public String modifyProgramSubmit(){
		if(filename!=null){
			program.setImage(request.getContextPath()+"/upload/"+filename);
			String realPath = ServletActionContext.getRequest().getRealPath(root);
			String targetDirectory = realPath;
			targetFileName = filename;
			setDir(targetDirectory + "\\" + targetFileName);
			if(!dir.substring(dir.length()-3,dir.length()).equals("jpg")){
				resultmsg = "file type error, please upload an image";
				System.out.println(resultmsg);
				return INPUT;
			}
			File target = new File(targetDirectory,targetFileName);
			try {
				FileUtils.copyFile(imagefile, target);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(program.getProgramid());
		} 
		System.out.println("origin filename："+program.getImage());
		manageProgramService.modifyProgram(program);
		programName = "";
		return SUCCESS;
	}
