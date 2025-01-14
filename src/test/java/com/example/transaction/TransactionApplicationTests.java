package com.example.transaction;

import com.example.transaction.model.Transaction;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@Order(1)
	void createRepeat() throws Exception {
		int id = 1;
		String sender = "s1";
		String receiver = "r1";
		int amount = 1;
		String currency = "CNY";
		Transaction transaction = new Transaction(id,sender,receiver,amount,currency);
		String jsonTransaction = objectMapper.writeValueAsString(transaction);

		//插入第一条
		mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonTransaction))
				.andExpect(status().isOk());
		//插入重复id，bad request
		mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonTransaction))
				.andExpect(status().isBadRequest());

		//插入金额<0，bad request
		transaction.setAmount(-1);
		jsonTransaction = objectMapper.writeValueAsString(transaction);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonTransaction))
				.andExpect(status().isBadRequest());

		mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].sender").value(sender))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].receiver").value(receiver))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].amount").value(amount))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].currency").value(currency));

		id = 2;
		sender = "s2";
		receiver = "r2";
		amount = 2;
		currency = "USD";
		transaction = new Transaction(id,sender,receiver,amount,currency);
		jsonTransaction = objectMapper.writeValueAsString(transaction);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonTransaction));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].sender").value("s1"))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].receiver").value("r1"))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].amount").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].currency").value("CNY"))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[1].id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[1].sender").value(sender))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[1].receiver").value(receiver))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[1].amount").value(amount))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[1].currency").value(currency));
	}

	@Test
	@Order(2)
	void deleteRepeat() throws Exception {
		int id = 1;
		String sender = "s1";
		String receiver = "r1";
		int amount = 1;
		String currency = "CNY";
		Transaction transaction = new Transaction(id,sender,receiver,amount,currency);
		String jsonTransaction = objectMapper.writeValueAsString(transaction);

		//插入第一条
		mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonTransaction))
				.andExpect(status().isOk());
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/transactions/%d".formatted(id)))
				.andExpect(status().isOk());
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/transactions/%d".formatted(id)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Order(3)
	void modify() throws Exception {
		int id = 2;
		String sender = "s1";
		String receiver = "r1";
		int amount = 1;
		String currency = "CNY";
		Transaction transaction = new Transaction(id,sender,receiver,amount,currency);
		String jsonTransaction = objectMapper.writeValueAsString(transaction);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonTransaction));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].sender").value(sender))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].receiver").value(receiver))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].amount").value(amount))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].currency").value(currency));

		sender = "s2";
		receiver = "r2";
		amount = 2;
		currency = "USD";
		Transaction updateTransaction = new Transaction(id,sender,receiver,amount,currency);
		jsonTransaction = objectMapper.writeValueAsString(updateTransaction);

		mockMvc.perform(MockMvcRequestBuilders.put("/api/transactions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonTransaction));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].sender").value(sender))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].receiver").value(receiver))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].amount").value(amount))
				.andExpect(MockMvcResultMatchers.jsonPath("transactions.[0].currency").value(currency));
	}
}