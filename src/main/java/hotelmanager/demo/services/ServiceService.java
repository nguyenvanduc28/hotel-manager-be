package hotelmanager.demo.services;

import hotelmanager.demo.dto.BookingServiceOrderDto;
import hotelmanager.demo.dto.OrderItemDto;
import hotelmanager.demo.dto.ServiceDto;
import hotelmanager.demo.dto.ServiceItemDto;
import hotelmanager.demo.dto.ServiceCountDto;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.BookingServiceOrder;
import hotelmanager.demo.models.OrderItem;
import hotelmanager.demo.models.ServiceHotel;
import hotelmanager.demo.models.ServiceItem;
import hotelmanager.demo.models.enums.BookingServiceOrderStatus;
import hotelmanager.demo.repositories.BookingServiceOrderRepository;
import hotelmanager.demo.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotelmanager.demo.repositories.ServiceRepository;
import hotelmanager.demo.repositories.ServiceItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceItemRepository serviceItemRepository;
    @Autowired
    private BookingServiceOrderRepository bookingServiceOrderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public void initServiceType(int hotelId ){
        ServiceHotel service = new ServiceHotel();
        service.setHotelId(hotelId);
        service.setName("Dịch vụ phòng");
        service.setServiceType("ROOM");
        service.setDescription("Dịch vụ liên quan đến phòng");
        serviceRepository.save(service);

        ServiceHotel service2 = new ServiceHotel();
        service2.setHotelId(hotelId);
        service2.setName("Nhà hàng");
        service2.setServiceType("RESTAURANT");
        service2.setDescription("Dịch vụ phục vụ ăn uống nhà hàng khách sạn");
        serviceRepository.save(service2);

        ServiceHotel service3 = new ServiceHotel();
        service3.setHotelId(hotelId);
        service3.setName("Quầy bar");
        service3.setServiceType("BAR");
        service3.setDescription("Dịch vụ phục vụ uống nước, đồ uống tại quầy bar");
        serviceRepository.save(service3);
    }

    @Transactional
    public List<ServiceDto> getServiceList(int hotelId){
        return serviceRepository.findAllByHotelId(hotelId)
                .stream()
                .map(service -> modelMapper.map(service, ServiceDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ServiceItemDto> getAllServiceItemByServiceType(int serviceTypeId, int hotelId){
        ServiceHotel service = serviceRepository.findById(serviceTypeId).orElse(null);
        if(service == null){
            throw new RuntimeException("Service type not found");
        }
        return serviceItemRepository.findAllByHotelIdAndServiceTypeId(hotelId, service.getId())
                .stream()
                .map(serviceItem -> modelMapper.map(serviceItem, ServiceItemDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceDto createServiceType(ServiceDto serviceDto, int hotelId){
        ServiceHotel service = modelMapper.map(serviceDto, ServiceHotel.class);
        service.setHotelId(hotelId);
        serviceRepository.save(service);
        return modelMapper.map(service, ServiceDto.class);
    }

    @Transactional
    public ServiceItemDto createServiceItem(ServiceItemDto serviceItemDto, int hotelId){
        //check not found service type
        ServiceHotel service = serviceRepository.findById(serviceItemDto.getServiceType().getId()).orElse(null);
        if(service == null){
            throw new RuntimeException("Service type not found");
        }
        ServiceItem serviceItem = modelMapper.map(serviceItemDto, ServiceItem.class);
        serviceItem.setHotelId(hotelId);
        serviceItem.setServiceTypeId(service.getId());
        serviceItemRepository.save(serviceItem);
        return modelMapper.map(serviceItem, ServiceItemDto.class);
    }

    @Transactional
    public ServiceItemDto updateServiceItem(ServiceItemDto serviceItemDto, int hotelId){
        ServiceItem serviceItem = serviceItemRepository.findById(serviceItemDto.getId()).orElse(null);
        if(serviceItem == null){
            throw new RuntimeException("Service item not found");
        }
        //check not found service type
        ServiceHotel service = serviceRepository.findById(serviceItemDto.getServiceType().getId()).orElse(null);
        if(service == null){
            throw new RuntimeException("Service type not found");
        }
        serviceItem = modelMapper.map(serviceItemDto, ServiceItem.class);
        serviceItem.setHotelId(hotelId);
        serviceItem.setServiceTypeId(service.getId());
        serviceItemRepository.save(serviceItem);
        return modelMapper.map(serviceItem, ServiceItemDto.class);
    }

    @Transactional
    public ServiceDto updateServiceType(ServiceDto serviceDto, int hotelId){
        ServiceHotel service = serviceRepository.findById(serviceDto.getId()).orElse(null);
        if(service == null){
            throw new RuntimeException("Service type not found");
        }
        service = modelMapper.map(serviceDto, ServiceHotel.class);
        service.setHotelId(hotelId);
        serviceRepository.save(service);
        return modelMapper.map(service, ServiceDto.class);
    }

    @Transactional
    public List<BookingServiceOrderDto> getBookingServiceOrderByStatus(String status, int hotelId, int serviceTypeId) {
        List<BookingServiceOrder> bookingServiceOrders = bookingServiceOrderRepository.findAllByStatusAndHotelIdAndServiceTypeId(status, hotelId, serviceTypeId);
        List<BookingServiceOrderDto> bookingServiceOrderDtos = new ArrayList<>();

        for (BookingServiceOrder bookingServiceOrder : bookingServiceOrders) {
            BookingServiceOrderDto orderDto = modelMapper.map(bookingServiceOrder, BookingServiceOrderDto.class);
            
            List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(bookingServiceOrder.getId());
            List<OrderItemDto> orderItemDtos = new ArrayList<>();

            for (OrderItem orderItem : orderItems) {
                OrderItemDto itemDto = modelMapper.map(orderItem, OrderItemDto.class);
                
                ServiceItem serviceItem = serviceItemRepository.findById(orderItem.getServiceItemId())
                        .orElseThrow(() -> new NotFoundException("Service item not found"));
                ServiceItemDto serviceItemDto = modelMapper.map(serviceItem, ServiceItemDto.class);
                
                ServiceHotel serviceType = serviceRepository.findById(serviceItem.getServiceTypeId())
                        .orElseThrow(() -> new NotFoundException("Service type not found"));
                
                ServiceDto serviceTypeDto = modelMapper.map(serviceType, ServiceDto.class);
                
                serviceItemDto.setServiceType(serviceTypeDto);
                itemDto.setServiceItem(serviceItemDto);
                orderItemDtos.add(itemDto);
            }

            orderDto.setOrderItems(orderItemDtos);
            bookingServiceOrderDtos.add(orderDto);
        }

        return bookingServiceOrderDtos;
    }

    // cập nhật trạng thái của order
    @Transactional
    public void updateBookingServiceOrderStatus(int orderId, String status) {
        bookingServiceOrderRepository.updateStatus(orderId, status);
    }

    // lấy số lượng order theo trạng thái
    @Transactional
    public ServiceCountDto getServiceCount(int hotelId, int serviceTypeId) {
        ServiceCountDto serviceCountDto = new ServiceCountDto();
        
        // Get all orders for the hotel
        List<BookingServiceOrder> orders = bookingServiceOrderRepository.findAllOrderAvailableWithServiceTypeId(hotelId, serviceTypeId);

        serviceCountDto.setNumOfNewOrder(orders.stream().filter(order -> order.getStatus().equals(BookingServiceOrderStatus.NEW)).count());
        serviceCountDto.setNumOfInProgressOrder(orders.stream().filter(order -> order.getStatus().equals(BookingServiceOrderStatus.IN_PROGRESS)).count());
        serviceCountDto.setNumOfReadyToServeOrder(orders.stream().filter(order -> order.getStatus().equals(BookingServiceOrderStatus.READY_TO_SERVE)).count());
        return serviceCountDto;
    }
}
