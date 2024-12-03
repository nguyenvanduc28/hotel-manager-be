package hotelmanager.demo.configs;

import hotelmanager.demo.models.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/admin/auth/**").permitAll()
//                        .requestMatchers("/admin/upload/**").hasAuthority(RoleType.ADMIN.name())
//
                        .requestMatchers(HttpMethod.GET, "/admin/rooms/consumable-category").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "/admin/rooms/consumable-category").hasAnyAuthority(RoleType.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/admin/rooms/consumable/available").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "/admin/rooms/consumable/").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "/admin/rooms/consumable").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/admin/rooms/consumable-list").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/rooms/consumable").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/admin/rooms/consumable/{id}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "/admin/rooms/consumable/room/{roomId}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        
                        .requestMatchers(HttpMethod.GET, "/admin/rooms/equipment-category").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "/admin/rooms/equipment-category").hasAnyAuthority(RoleType.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/admin/rooms/equipment/available").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "/admin/rooms/equipment").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/admin/rooms/equipment-list").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/rooms/equipment").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/admin/rooms/equipment/{id}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "/admin/rooms/equipment/room/{roomId}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())

                        .requestMatchers(HttpMethod.GET, "/admin/rooms/roomtype").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "/admin/rooms/roomtype").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/admin/rooms/roomtype/price").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/rooms/roomtype/price").hasAnyAuthority(RoleType.ADMIN.name())
                        
                        .requestMatchers(HttpMethod.GET, "/admin/rooms/getall").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "/admin/rooms/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/admin/rooms/create").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/rooms/update").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())

                        .requestMatchers(HttpMethod.GET, "/admin/customers/getall").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "/admin/customers/search").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "/admin/customers/create").permitAll()

                        .requestMatchers(HttpMethod.GET, "admin/bookings/getall").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "admin/bookings/{id}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "admin/bookings/search-cusname").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "admin/bookings/search").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "admin/bookings/create").permitAll()
                        .requestMatchers(HttpMethod.POST, "admin/bookings/confirm/{id}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "admin/bookings/checkin/{id}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "admin/bookings/{bookingId}/consumables").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "admin/bookings/{bookingId}/consumable").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "admin/bookings/{bookingId}/equipment-damaged").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "admin/bookings/{bookingId}/equipment-damaged/single").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "admin/bookings/{bookingId}/checkout").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "admin/bookings/{bookingId}/unconfirm").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "admin/bookings/{bookingId}/uncheckin").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())

                        .requestMatchers(HttpMethod.GET, "/admin/bookings/{bookingId}/services").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                        .requestMatchers(HttpMethod.GET, "/admin/bookings/{bookingId}/service-order").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                        .requestMatchers(HttpMethod.POST, "/admin/bookings/{bookingId}/service-order").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/bookings/{orderId}/service-order").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                        .requestMatchers(HttpMethod.POST, "/admin/bookings/{orderId}/confirm-serviced").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                        .requestMatchers(HttpMethod.POST, "/admin/bookings/{orderId}/change-status").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                        .requestMatchers(HttpMethod.POST, "/admin/bookings/{orderId}/delete").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())

                        .requestMatchers(HttpMethod.GET, "/admin/invoices/getall").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "/admin/invoices/{id}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "/admin/invoices/customer/{customerId}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "/admin/invoices/booking/{bookingId}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "/admin/invoices/create").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.GET, "/admin/invoices/check-invoice-exists/{bookingId}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())

                        .requestMatchers(HttpMethod.GET, "/api/images/room/{roomId}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "/api/images/upload").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.POST, "/api/images/upload/multiple").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/images/{publicId}").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name())

                        .requestMatchers(HttpMethod.POST, "/admin/hotels/create").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/hotels/update/{id}").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/admin/hotels/get/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/admin/hotels/get-info-hotel").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/search/hotels").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/search/room/avai").permitAll()
                        .requestMatchers(HttpMethod.POST, "/admin/auth/register-admin").permitAll()

                        .requestMatchers(HttpMethod.GET, "/admin/employee/get-employee-info").hasAnyAuthority(RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name(), RoleType.ADMIN.name(), RoleType.RECEPTIONIST.name(), RoleType.WAREHOUSE_MANAGER.name(), RoleType.STAFF_MANAGER.name(), RoleType.CUSTOMER_MANAGER.name(), RoleType.SERVICE_MANAGER.name(), RoleType.REPORT_MANAGER.name(), RoleType.INVOICE_MANAGER.name(), RoleType.HOTEL_INFO_MANAGER.name(), RoleType.SERVICE_COUNTER.name())
                        .requestMatchers(HttpMethod.GET, "/admin/employee/getall2").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/admin/employee/{id}").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/admin/employee/create").hasAnyAuthority(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/employee/update/{id}").hasAnyAuthority(RoleType.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/admin/service/service-type-list").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                        .requestMatchers(HttpMethod.POST, "/admin/service/create-service-type").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SERVICE_MANAGER.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/service/update-service-type").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SERVICE_MANAGER.name())

                        .requestMatchers(HttpMethod.GET, "/admin/service/service-item-list").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                        .requestMatchers(HttpMethod.POST, "/admin/service/create-service-item").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SERVICE_MANAGER.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/service/update-service-item").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SERVICE_MANAGER.name())

                        .requestMatchers(HttpMethod.PUT, "/admin/service/update-booking-service-order-status").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                        .requestMatchers(HttpMethod.GET, "/admin/service/booking-service-order-list").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                        .requestMatchers(HttpMethod.GET, "/admin/service/service-count").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SERVICE_MANAGER.name(), RoleType.SERVICE_COUNTER.name(), RoleType.BAR_COUNTER.name(), RoleType.RESTAURANT_COUNTER.name())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
