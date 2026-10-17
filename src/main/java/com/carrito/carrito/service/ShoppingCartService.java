package com.carrito.carrito.service;

import com.carrito.carrito.dto.ShoppingCartResponse;
import com.carrito.carrito.entity.Producto;
import com.carrito.carrito.entity.ShoppingCart;
import com.carrito.carrito.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductoService productoService;

    public ShoppingCartService(
            ShoppingCartRepository shoppingCartRepository,
            ProductoService productoService) {

        this.shoppingCartRepository = shoppingCartRepository;
        this.productoService = productoService;
    }

    public List<ShoppingCartResponse> obtenerCarrito(String codUsuario) {
        return shoppingCartRepository.findByCodUsuario(codUsuario)
                .stream()
                .map(item -> {
                    Producto producto = productoService
                            .obtenerPorId(item.getIdProducto())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "El producto con ID "
                                                    + item.getIdProducto()
                                                    + " no existe"));

                    return new ShoppingCartResponse(
                            item.getIdShoppingCart(),
                            producto.getIdProducto(),
                            item.getCodUsuario(),
                            producto.getNombre(),
                            producto.getCodigo(),
                            producto.getImagen(),
                            producto.getPrecio(),
                            item.getCantidad()
                    );
                })
                .toList();
    }

    @Transactional
    public ShoppingCart agregarProducto(
            String codUsuario, Integer idProducto, Integer cantidad) {

        if (codUsuario == null || codUsuario.isBlank()) {
            throw new IllegalArgumentException(
                    "El código de usuario es obligatorio");
        }

        if (idProducto == null || idProducto <= 0) {
            throw new IllegalArgumentException(
                    "El ID del producto no es válido");
        }

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        if (productoService.obtenerPorId(idProducto).isEmpty()) {
            throw new IllegalArgumentException(
                    "El producto con ID " + idProducto + " no existe");
        }

        ShoppingCart item = shoppingCartRepository
                .findByCodUsuarioAndIdProducto(codUsuario, idProducto)
                .orElseGet(ShoppingCart::new);

        item.setCodUsuario(codUsuario);
        item.setIdProducto(idProducto);

        if (item.getIdShoppingCart() == null) {
            item.setCantidad(cantidad);
        } else {
            item.setCantidad(item.getCantidad() + cantidad);
        }

        return shoppingCartRepository.save(item);
    }

    @Transactional
    public void eliminarProducto(String codUsuario, Integer idProducto) {
        ShoppingCart item = shoppingCartRepository
                .findByCodUsuarioAndIdProducto(codUsuario, idProducto)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El producto no existe en el carrito"));

        shoppingCartRepository.delete(item);
    }

    @Transactional
    public void vaciarCarrito(String codUsuario) {
        List<ShoppingCart> productos =
                shoppingCartRepository.findByCodUsuario(codUsuario);

        shoppingCartRepository.deleteAll(productos);
    }
}
