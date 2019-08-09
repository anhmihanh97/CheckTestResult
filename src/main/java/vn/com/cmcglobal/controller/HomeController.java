package vn.com.cmcglobal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.com.cmcglobal.dto.TestCaseDTO;

@Controller
public class HomeController {
	@GetMapping(value="/")
	public String home(Model model) {
		//model.addAttribute("user", new User());
		//List<User> users = springReadFileService.findAll();
		//model.addAttribute("users", users);
		return "view/home";
	}
	@PostMapping(value = "/fileupload")
	public String uploadFile(@ModelAttribute TestCaseDTO user, RedirectAttributes redirectAttributes, Model model) {
	// 	boolean isFlag = springReadFileService.saveDataFromUploadfile(user.getFile());
		boolean isFlag = true;
		if(isFlag) {
			redirectAttributes.addFlashAttribute("successmessage", "File Upload Successfully!");
			 model.addAttribute("successmessage1", "sggwret");
		}else {
			redirectAttributes.addFlashAttribute("errormessage", "File Upload not done, Please try again!");
			model.addAttribute("errormessage", "thong bao that bai");
		}
		return "redirect:/";
	}
	
	
}
