package com.carrito.carrito.dto;

import java.math.BigDecimal;

public class ShoppingCartResponse {

    private Integer idShoppingCart;
    private Integer idProducto;
    private String codUsuario;
    private String nombre;
    private String codigo;
    private String imagen;
    private BigDecimal precio;
    private Integer cantidad;
    private BigDecimal subTotal;

    public ShoppingCartResponse(
            Integer idShoppingCart,
            Integer idProducto,
            String codUsuario,
            String nombre,
            String codigo,
            String imagen,
            BigDecimal precio,
            Integer cantidad) {

        this.idShoppingCart = idShoppingCart;
        this.idProducto = idProducto;
        this.codUsuario = codUsuario;
        this.nombre = nombre;
        this.codigo = codigo;
        this.imagen = imagen;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subTotal = precio.multiply(
                BigDecimal.valueOf(cantidad));
    }

    public Integer getIdShoppingCart() {
        return idShoppingCart;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getImagen() {
        return imagen;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }
}
