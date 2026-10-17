package com.carrito.carrito.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idShoppingCart")
    private Integer idShoppingCart;

    @Column(name = "idProducto")
    private Integer idProducto;

    @Column(name = "codUsuario", length = 128)
    private String codUsuario;

    @Column(name = "cantidad")
    private Integer cantidad;

    public ShoppingCart() {
    }

    public Integer getIdShoppingCart() {
        return idShoppingCart;
    }

    public void setIdShoppingCart(Integer idShoppingCart) {
        this.idShoppingCart = idShoppingCart;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
