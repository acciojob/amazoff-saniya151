package com.driver;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository repoObj = new OrderRepository();
    public void addOrder(Order order) {
        repoObj.addOrder(order);
    }

    public void addDeliveryPartner(String partnerId) {
        DeliveryPartner dp = new DeliveryPartner(partnerId);
        repoObj.addPartner(dp);
    }

    public void assignOrderToPartner(String orderId, String partnerId) {
        repoObj.addOrderPartnerPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        return repoObj.getOrderById(orderId);
    }

    public DeliveryPartner getPartner(String partnerId) {
        return repoObj.getPartnerById(partnerId);
    }

    public int ordersAssignedToPartner(String partnerId) {
        return repoObj.orderCountByPartnerId(partnerId);
    }

    public List<String> orderListByPartnerId(String partnerId) {
        return repoObj.orderListByPartnerId(partnerId);
    }

    public List<String> allOrders() {
        return repoObj.allOrders();
    }

    public int getCountOfUnassignedOrders() {
        return repoObj.getCountOfUnassignedOrders();
    }

    public int ordersLeftAfter(String deliveryTime, String partnerId) {
        return repoObj.ordersLeftAfterTime(deliveryTime, partnerId);
    }

    public String lastDelivery(String partnerId) {
        return repoObj.lastDeliveryTime(partnerId);
    }

    public void deletePartner(String partnerId) {
        repoObj.deletePartnerById(partnerId);
    }

    public void deleteOrder(String orderId) {
        repoObj.deleteOrderById(orderId);
    }

}