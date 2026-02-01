package in.hotel.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.hotel.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);
}
