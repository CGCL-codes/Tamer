    @RequestMapping(value = "saveOrUpdateProduct.do", method = RequestMethod.POST)
    public String saveUpdateProduct(HttpServletRequest request, ModelMap modelMap, @ModelAttribute("ProductDTO") final ProductDTO productDTO, @RequestParam("productImage") final MultipartFile productImage, @RequestParam("brand") final Integer brand, @RequestParam("category") final Integer category) {
        if (!this.isLoggedIn(request)) {
            return "redirect:/authentication/notLoggedIn.do";
        }
        Boolean result = false;
        File fileProductImage = new File(request.getSession().getServletContext().getRealPath("/img/products/") + File.separatorChar + productDTO.getCode() + ".JPG");
        File dirProductImagesPj = new File("D:/Desarrollos_Experimentos/Experimentos/little-store/src/main/webapp/img/products/");
        log.debug("Save or Update products");
        log.debug("fileProductImage=" + fileProductImage);
        productDTO.getBrandDTO().setIdBrand(brand);
        productDTO.getCategoryDTO().setIdCategory(category);
        log.debug("productDTO= " + productDTO + ", idBrand= " + brand + ", idCategory=" + category);
        try {
            if (productDTO.getIdProduct() == null) {
                result = this.catalogsService.addProduct(productDTO);
            } else {
                result = this.catalogsService.modifyProduct(productDTO);
            }
            if (result) {
                log.debug("Uploading Image");
                log.debug("FileUpload= " + productImage + ", " + productImage.getContentType() + ", " + productImage.getName() + ", " + productImage.getOriginalFilename() + ", " + productImage.getSize());
                FileUtils.writeByteArrayToFile(fileProductImage, productImage.getBytes());
                FileUtils.copyFileToDirectory(fileProductImage, dirProductImagesPj);
            }
        } catch (ApplicationException ex) {
            log.error(ex);
            modelMap.put(GenericConstants.KEY_ERROR, "ErrorCode " + ex.getErrorCode() + ": " + ex.getErrorMessage());
        } catch (Exception ex) {
            log.error(ex);
            modelMap.put(GenericConstants.KEY_FATAL, ex.getMessage());
        }
        if (modelMap.get(GenericConstants.KEY_ERROR) == null) {
            modelMap.put(GenericConstants.KEY_INFO, productDTO.getName());
        }
        return "admin/products/add_update_result";
    }
