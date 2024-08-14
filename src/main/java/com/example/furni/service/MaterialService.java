package com.example.furni.service;

import com.example.furni.entity.Material;
import com.example.furni.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public Optional<Material> getMaterialById(int id) {
        return materialRepository.findById(id);
    }
    public Page<Material> getMaterialsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return materialRepository.findAll(pageable);
    }
    public Material saveMaterial(Material material) {
        return materialRepository.save(material);
    }

    public Material updateMaterial(int id, Material materialDetails) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new RuntimeException("Material not found"));
        material.setMaterialName(materialDetails.getMaterialName());

        return materialRepository.save(material);
    }

    public void deleteMaterial(int id) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new RuntimeException("Material not found"));
        materialRepository.delete(material);
    }
}
