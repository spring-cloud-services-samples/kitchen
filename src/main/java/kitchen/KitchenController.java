/*
 * Copyright 2025-Present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kitchen;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class KitchenController {

	private static final String SESSION_EMAIL = "userEmail";

	private final DishService dishService;
	private final ThemeService themeService;
	private final PriceTierService priceTierService;

	public KitchenController(DishService dishService, ThemeService themeService, PriceTierService priceTierService) {
		this.dishService = dishService;
		this.themeService = themeService;
		this.priceTierService = priceTierService;
	}

	@GetMapping("/")
	public String index() {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("loginForm", new LoginForm(""));
		model.addAttribute("theme", this.themeService.getTheme());
		return "login";
	}

	@PostMapping("/login")
	public RedirectView loginSubmit(@ModelAttribute LoginForm loginForm, HttpSession session) {
		session.setAttribute(SESSION_EMAIL, loginForm.email());
		return new RedirectView("/home", true);
	}

	@GetMapping("/home")
	public String home(Model model, HttpSession session) {
		String email = (String) session.getAttribute(SESSION_EMAIL);
		model.addAttribute("userEmail", email != null ? email : "Guest");
		model.addAttribute("dishes", this.dishService.getDishes());
		model.addAttribute("betaDishes", this.dishService.getBetaDishes(email));
		model.addAttribute("theme", this.themeService.getTheme());
		model.addAttribute("discountPercent", this.priceTierService.discountPercent(session.getId()));
		return "home";
	}

	@GetMapping("/logout")
	public RedirectView logout(HttpSession session) {
		session.invalidate();
		return new RedirectView("/login", true);
	}

	public record LoginForm(String email) {
	}
}
