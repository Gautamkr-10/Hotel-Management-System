package in.hotel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.hotel.dto.Response;
import in.hotel.dto.RoomDTO;
import in.hotel.entity.Room;
import in.hotel.exception.OurException;
import in.hotel.repo.BookingRepository;
import in.hotel.repo.RoomRepository;
import in.hotel.service.interfac.CloudinaryService;
import in.hotel.service.interfac.IRoomService;
import in.hotel.utils.Utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // ---------------- ADD ROOM ----------------
    @Override
    public Response addNewRoom(MultipartFile photo, String roomType,
                               BigDecimal roomPrice, String description) {

        Response response = new Response();

        try {
            if (photo == null || photo.isEmpty())
                throw new OurException("Room photo is required");

            if (roomType == null || roomType.isBlank())
                throw new OurException("Room type is required");

            if (roomPrice == null)
                throw new OurException("Room price is required");

            String imageUrl = cloudinaryService.uploadImage(photo);

            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);

            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);

            response.setStatusCode(200);
            response.setMessage("Room added successfully");
            response.setRoom(roomDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving room: " + e.getMessage());
        }
        return response;
    }

    // ---------------- DELETE ROOM ----------------
    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();

        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room Not Found"));

            if (room.getRoomPhotoUrl() != null)
                cloudinaryService.deleteImage(room.getRoomPhotoUrl());

            roomRepository.deleteById(roomId);

            response.setStatusCode(200);
            response.setMessage("Room deleted successfully");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting room: " + e.getMessage());
        }
        return response;
    }

    // ---------------- UPDATE ROOM ----------------
    @Override
    public Response updateRoom(Long roomId, String description,
                               String roomType, BigDecimal roomPrice,
                               MultipartFile photo) {

        Response response = new Response();

        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room Not Found"));

            if (photo != null && !photo.isEmpty()) {
                if (room.getRoomPhotoUrl() != null)
                    cloudinaryService.deleteImage(room.getRoomPhotoUrl());

                String imageUrl = cloudinaryService.uploadImage(photo);
                room.setRoomPhotoUrl(imageUrl);
            }

            if (roomType != null && !roomType.isBlank())
                room.setRoomType(roomType);

            if (roomPrice != null)
                room.setRoomPrice(roomPrice);

            if (description != null)
                room.setRoomDescription(description);

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("Room updated successfully");
            response.setRoom(roomDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating room: " + e.getMessage());
        }
        return response;
    }

    // ---------------- GET ALL ROOM TYPES ----------------
    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    // ---------------- GET ALL ROOMS ----------------
    @Override
    public Response getAllRooms() {
        Response response = new Response();
        try {
            List<Room> roomList =
                    roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

            List<RoomDTO> roomDTOList =
                    Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting rooms: " + e.getMessage());
        }
        return response;
    }

    // ---------------- GET ROOM BY ID ----------------
    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room Not Found"));

            RoomDTO roomDTO =
                    Utils.mapRoomEntityToRoomDTOPlusBookings(room);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // ---------------- AVAILABLE BY DATE + TYPE ----------------
    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkInDate,
                                                   LocalDate checkOutDate,
                                                   String roomType) {

        Response response = new Response();
        try {
            List<Room> availableRooms =
                    roomRepository.findAvailableRoomsByDatesAndTypes(
                            checkInDate, checkOutDate, roomType);

            List<RoomDTO> roomDTOList =
                    Utils.mapRoomListEntityToRoomListDTO(availableRooms);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // ---------------- ALL AVAILABLE ----------------
    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();
        try {
            List<Room> roomList =
                    roomRepository.getAllAvailableRooms();

            List<RoomDTO> roomDTOList =
                    Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
