package vn.com.cmcglobal.controller;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.com.cmcglobal.dto.TestCaseDTO;
import vn.com.cmcglobal.service.TestResultServiceV2;

@Controller
public class HomeController {
	@Autowired 
	TestResultServiceV2 testResultServiceV2;
	@GetMapping(value="/")
	public String home(Model model) {
		return "view/home";
	}
	@PostMapping(value = "/fileupload")
	public String uploadFile(@ModelAttribute("test") TestCaseDTO testCase, RedirectAttributes redirectAttributes, Model model) {
		try {
			testResultServiceV2.writeWrongResult(testCase.getFile(),testCase.getDateStart(),testCase.getDateEnd(), testCase.getST());
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return "redirect:/";
	}
	
	
}
