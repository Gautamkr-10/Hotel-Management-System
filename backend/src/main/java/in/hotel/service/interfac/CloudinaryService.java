package in.hotel.service.interfac;



import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile file);
    void deleteImage(String imageUrl);
}