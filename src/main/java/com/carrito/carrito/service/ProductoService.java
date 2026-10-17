package com.carrito.carrito.service;

import com.carrito.carrito.entity.Producto;
import com.carrito.carrito.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Obtener todos los productos
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // Buscar un producto por su identificador
    public Optional<Producto> obtenerPorId(Integer id) {
        return productoRepository.findById(id);
    }

    // Guardar o actualizar un producto
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // Eliminar un producto
    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }
}