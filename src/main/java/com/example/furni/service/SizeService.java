package com.example.furni.service;

import com.example.furni.entity.Category;
import com.example.furni.entity.Material;
import com.example.furni.entity.Size;
import com.example.furni.repository.ProductRepository;
import com.example.furni.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeService {

    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ProductRepository productRepository;
    public List<Size> getAllSize() {
        return sizeRepository.findAll();
    }

    public Optional<Size> findById(int id) {
        return sizeRepository.findById(id);
    }

    public Page<Size> getSizesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sizeRepository.findAll(pageable);
    }
    public Size save(Size size) {
        return sizeRepository.save(size);
    }
    public void deleteSize(int id) {
        // Kiểm tra xem có sản phẩm nào liên kết với category không
        if (productRepository.existsByCategory_Id(id)) {
            throw new IllegalStateException("The size cannot be deleted because there are already products in the size.");
        }

        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Size not found"));
        sizeRepository.delete(size);
    }

    public boolean isSizeNameExists(String sizeName) {
        return sizeRepository.existsBySizeName(sizeName);
    }

    public boolean isSizeNameExists(String sizeName, int excludeId) {
        return sizeRepository.existsBySizeNameAndIdNot(sizeName, excludeId);
    }
}
