package dev.fabianmild.ehifloesung.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fabianmild.ehifloesung.domain.entities.Article;
import dev.fabianmild.ehifloesung.domain.entities.Customer;
import dev.fabianmild.ehifloesung.domain.entities.Order;
import dev.fabianmild.ehifloesung.domain.enums.OrderStatus;
import dev.fabianmild.ehifloesung.domain.valueObjects.OrderLineItem;
import dev.fabianmild.ehifloesung.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static dev.fabianmild.ehifloesung.presentation.RestAPIRouteSupport.BASE_URL;
import static dev.fabianmild.ehifloesung.presentation.RestAPIRouteSupport.SLASH_;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    private @MockitoBean OrderService orderService;
    private @Autowired MockMvc mockMvc;
    private @Autowired ObjectMapper objectMapper;
    @Autowired
    private OrderRestController orderRestController;


    @Test
    void getAllOrders() throws Exception {
        var a = Article.builder()
                .articleNumber("nr")
                .itemsInStock(120L)
                .description("dr")
                .name("name")
                .unitPriceInCents(10L).build();
        var ol = OrderLineItem.builder()
                .name("name")
                .article(a)
                .articleNumber(a.getArticleNumber())
                .description("dr")
                .unitPriceInCents(12L)
                .quantity(4L)
                .build();
        var c = Customer.builder()
                .customerNumber("nr")
                .name("name")
                .build();
        var order = Order.builder()
                .orderNumber("HI")
                .orderLineItems(List.of(ol))
                .orderStatus(OrderStatus.PLACED)
                .placementDate(LocalDate.now())
                .customer(c)
                .build();

        when(orderService.getAll()).thenReturn(List.of(order));

        var request = get(BASE_URL + SLASH_ + OrderRestController.ORDER_ROUTE)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andDo(print());
    }

    @Test
    void getOrderByNumber() {
    }

    @Test
    void createOrder() {
    }
}