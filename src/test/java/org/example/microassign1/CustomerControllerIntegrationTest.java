package org.example.microassign1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepo customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer testCustomer;

    @BeforeEach
    public void setup() {
        customerRepository.deleteAll();

        testCustomer = new Customer();
        testCustomer.setName("Alice Wonderland");
        testCustomer.setEmail("alice@example.com");
        testCustomer = customerRepository.save(testCustomer);
    }

    @Test
    public void shouldGetCustomerById() throws Exception {
        mockMvc.perform(get("/customers/" + testCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alice Wonderland")));
    }

    @Test
    public void shouldGetAllCustomers() throws Exception {
        mockMvc.perform(get("/customers?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Alice Wonderland")));
    }

    @Test
    public void shouldCreateCustomer() throws Exception {
        Customer newCustomer = new Customer();
        newCustomer.setName("Bob Builder");
        newCustomer.setEmail("bob@example.com");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bob Builder")));
    }

    @Test
    public void shouldDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/" + testCustomer.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/customers/" + testCustomer.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundForMissingCustomer() throws Exception {
        mockMvc.perform(get("/customers/99999"))
                .andExpect(status().isNotFound());
    }
}
