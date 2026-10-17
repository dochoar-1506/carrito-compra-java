package com.carrito.carrito.repository;

import com.carrito.carrito.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository
        extends JpaRepository<ShoppingCart, Integer> {

    List<ShoppingCart> findByCodUsuario(String codUsuario);

    Optional<ShoppingCart> findByCodUsuarioAndIdProducto(
            String codUsuario, Integer idProducto);
}
