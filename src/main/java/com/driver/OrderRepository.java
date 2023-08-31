package com.driver;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    //orderId vs Order
    private Map<String, Order> orderMap = new HashMap<>();
    //partnerId, Partner
    private Map<String, DeliveryPartner> partnerMap = new HashMap<>();
    //partnerId vs List of orders
    private Map<String, List<String>> pairMap = new HashMap<>();
    //orderId vs partnerId
    private Map<String, String> orderAssignMap = new HashMap<>();

    public void addOrder(Order order) {
        String orderId = order.getId();
        orderMap.put(orderId, order);
    }

    public void addPartner(DeliveryPartner partner) {
        String partnerId = partner.getId();
        partnerMap.put(partnerId, partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {

        if(orderAssignMap.containsKey(orderId)) return;

        List<String> partnerOrders = pairMap.getOrDefault(partnerId, new ArrayList<String>());
        partnerOrders.add(orderId);

        DeliveryPartner partner = partnerMap.get(partnerId);
        int oldOrders = partner.getNumberOfOrders();
        partner.setNumberOfOrders(oldOrders + 1);

        pairMap.put(partnerId, partnerOrders);
        orderAssignMap.put(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        Order order = orderMap.get(orderId);
        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        DeliveryPartner partner = partnerMap.get(partnerId);
        return partner;
    }

    public int orderCountByPartnerId(String partnerId) {
        //find size of order list for partnerId
        DeliveryPartner partner = partnerMap.get(partnerId);
        int countOrders = partner.getNumberOfOrders();

        return countOrders;

    }

    public List<String> orderListByPartnerId(String partnerId) {
        List<String> partnerOrders = pairMap.getOrDefault(partnerId, new ArrayList<String>());
        return partnerOrders;
    }

    public List<String> allOrders() {
        List<String> orderList = new ArrayList<>(orderMap.keySet());
        return orderList;
    }

    public int getCountOfUnassignedOrders() {
        return (orderMap.size() - orderAssignMap.size());
    }

    public int ordersLeftAfterTime(String deliveryTime, String partnerId) {
        int time = 100*Integer.parseInt(deliveryTime.substring(0, 2))+ Integer.parseInt(deliveryTime.substring(3));
        List<String> partnerOrders = pairMap.get(partnerId);

        int count = 0;
        for(String orderId : partnerOrders) {
            if(orderMap.get(orderId).getDeliveryTime() > time) count++;
        }
        return count;
    }

    public String lastDeliveryTime(String partnerId) {

        List<String> partnerOrders = pairMap.get(partnerId);

        int lastOrderTime = 0000;
        String[] deliveryTimeArray = new String[5];

        for(String orderId : partnerOrders) {
            int orderDeliveryTime = orderMap.get(orderId).getDeliveryTime();
            if(orderDeliveryTime > lastOrderTime)
                lastOrderTime = orderDeliveryTime;
        }

        for(int i = 4; i >= 0; i--) {

            if(i == 2) {
                deliveryTimeArray[i] = ":";
            }
            else {
                int r = lastOrderTime % 10;
                lastOrderTime = lastOrderTime / 10;

                deliveryTimeArray[i] = String.valueOf(r);
            }
        }

        String deliveryTime = String.join("", deliveryTimeArray);
        return deliveryTime;
    }

    public void deletePartnerById(String partnerId) {
        if(!partnerMap.containsKey(partnerId)) return;

        partnerMap.remove(partnerId);

        if(!pairMap.containsKey(partnerId)) return;

        for(String orderId : pairMap.get(partnerId)) {
            orderAssignMap.remove(orderId);
        }
        pairMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        if(!orderMap.containsKey(orderId)) return;

        orderMap.remove(orderId);

        if(!orderAssignMap.containsKey(orderId)) return;

        String partnerId = orderAssignMap.get(orderId);

        DeliveryPartner partner = partnerMap.get(partnerId);
        int oldOrders = partner.getNumberOfOrders();
        partner.setNumberOfOrders(oldOrders - 1);

        List<String> partnerOrders = pairMap.get(partnerId);
        partnerOrders.remove(orderId);

        orderAssignMap.remove(orderId);
    }
}