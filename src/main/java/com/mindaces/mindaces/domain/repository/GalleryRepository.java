package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GalleryRepository extends JpaRepository<Gallery, Long>
{
    Gallery findByGalleryName(String galleryName);


}