package hotelmanager.demo.services;

import hotelmanager.demo.dto.ServiceDto;
import hotelmanager.demo.dto.ServiceItemDto;
import hotelmanager.demo.models.ServiceHotel;
import hotelmanager.demo.models.ServiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotelmanager.demo.repositories.ServiceRepository;
import hotelmanager.demo.repositories.ServiceItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceItemRepository serviceItemRepository;
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

}
