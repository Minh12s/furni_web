package com.example.furni.service;

import com.example.furni.entity.Size;
import com.example.furni.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeService {

    @Autowired
    private SizeRepository sizeRepository;
    public List<Size> getAllSize() {
        return sizeRepository.findAll();
    }
    public Page<Size> findAll(Pageable pageable) {
        return sizeRepository.findAll(pageable);
    }

    public Optional<Size> findById(int id) {
        return sizeRepository.findById(id);
    }

    public Size save(Size size) {
        return sizeRepository.save(size);
    }

    public void deleteById(int id) {
        sizeRepository.deleteById(id);
    }
}
