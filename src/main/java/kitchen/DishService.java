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

import java.util.List;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.EvaluationContext;
import dev.openfeature.sdk.MutableContext;

import org.springframework.stereotype.Service;

@Service
public class DishService {
	private static final List<Dish> DAILY_DISHES = List.of(
			new Dish("Grilled Salmon", "Fresh Atlantic salmon with herbs and lemon"),
			new Dish("Caesar Salad", "Crisp romaine, parmesan, croutons, and classic Caesar dressing"),
			new Dish("Vegetable Stir-Fry", "Seasonal vegetables in a light soy-ginger sauce"),
			new Dish("Tomato Basil Soup", "Creamy soup with fresh basil and a hint of garlic")
	);

	private static final List<Dish> BETA_DISHES = List.of(
			new Dish("Lobster Thermidor", "Classic French lobster with brandy and cheese"),
			new Dish("Truffle Risotto", "Arborio rice with black truffle and parmesan")
	);

	private final Client client;

	public DishService(Client client) {
		this.client = client;
	}


	List<Dish> getDishes() {
		return DAILY_DISHES;
	}

	List<Dish> getBetaDishes(String email) {
		EvaluationContext context = new MutableContext().add("email", email);

		if (this.client.getBooleanValue("beta-dishes", false, context)) {
			return BETA_DISHES;
		}
		return List.of();
	}

	public record Dish(String name, String description) {
	}
}
