package com.carrito.carrito.controller;

import com.carrito.carrito.dto.ShoppingCartResponse;
import com.carrito.carrito.entity.ShoppingCart;
import com.carrito.carrito.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/{codUsuario}")
public List<ShoppingCartResponse> obtenerCarrito(
        @PathVariable String codUsuario) {
    return shoppingCartService.obtenerCarrito(codUsuario);
}

    @PostMapping("/{codUsuario}/agregar")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCart agregarProducto(
            @PathVariable String codUsuario,
            @RequestBody Map<String, Integer> datos) {

        Integer idProducto = datos.get("idProducto");
        Integer cantidad = datos.get("cantidad");

        return shoppingCartService.agregarProducto(
                codUsuario, idProducto, cantidad);
    }

    @DeleteMapping("/{codUsuario}/producto/{idProducto}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(
            @PathVariable String codUsuario,
            @PathVariable Integer idProducto) {

        shoppingCartService.eliminarProducto(codUsuario, idProducto);
    }

    @DeleteMapping("/{codUsuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vaciarCarrito(@PathVariable String codUsuario) {
        shoppingCartService.vaciarCarrito(codUsuario);
    }
}
