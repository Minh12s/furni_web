package com.example.furni.service;

import com.example.furni.entity.Category;
import com.example.furni.entity.Material;
import com.example.furni.repository.MaterialRepository;
import com.example.furni.repository.ProductRepository;
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
    @Autowired
    private ProductRepository productRepository;

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
        // Kiểm tra xem có sản phẩm nào liên kết với category không
        if (productRepository.existsByMaterial_Id(id)) {
            throw new IllegalStateException("The material cannot be deleted because there are already products in the material.");
        }

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));
        materialRepository.delete(material);
    }
    public boolean isMaterialNameExists(String materialName) {
        return materialRepository.existsByMaterialName(materialName);
    }

    public boolean isMaterialNameExists(String materialName, int excludeId) {
        return materialRepository.existsByMaterialNameAndIdNot(materialName, excludeId);
    }
}
