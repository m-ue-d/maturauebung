package dev.fabianmild.ehifloesung.service;

import dev.fabianmild.ehifloesung.domain.RevenueDerivationReport;
import dev.fabianmild.ehifloesung.domain.RevenueDerivationReportElement;
import dev.fabianmild.ehifloesung.domain.entities.Customer;
import dev.fabianmild.ehifloesung.domain.enums.OrderStatus;
import dev.fabianmild.ehifloesung.persistence.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public RevenueDerivationReport determineRevenueDerivation(Customer c, LocalDate start, LocalDate end) {
        var orders = orderRepository.findByCustomerAndPlacementDateBetween(c, start, end)
                .stream()
                .filter(order -> order.getOrderStatus() != OrderStatus.DELIVERED);

        return new RevenueDerivationReport(c, orders.flatMap(order ->
                        order.getOrderLineItems().stream().map(orderLineItem ->
                                    new RevenueDerivationReportElement(order,
                                            orderLineItem.getArticle(),
                                            orderLineItem.getArticle().getUnitPriceInCents() - orderLineItem.getUnitPriceInCents(),
                                            orderLineItem.getUnitPriceInCents()
                                            )
                        )).toList()
                );
    }
}
