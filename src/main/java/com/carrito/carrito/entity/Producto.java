package com.carrito.carrito.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "idProducto")
private Integer idProducto;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen;

    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;

    public Producto() {
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
