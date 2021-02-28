package com.minskim.springboots3cloudfrontex.domain.repository;

import com.minskim.springboots3cloudfrontex.domain.entity.GalleryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryRepository extends JpaRepository<GalleryEntity, Long> {
    @Override
    List<GalleryEntity> findAll();
}