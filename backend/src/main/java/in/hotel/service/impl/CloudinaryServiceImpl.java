package in.hotel.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import in.hotel.service.interfac.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "hotel_rooms")
            );
            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Cloudinary upload failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            String publicId = imageUrl
                    .substring(imageUrl.indexOf("hotel_rooms/"))
                    .replace("hotel_rooms/", "")
                    .replace(".jpg", "")
                    .replace(".png", "");

            cloudinary.uploader().destroy("hotel_rooms/" + publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Cloudinary delete failed: " + e.getMessage());
        }
    }
}
