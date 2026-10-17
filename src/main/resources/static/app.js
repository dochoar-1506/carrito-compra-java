"use strict";

const API_PRODUCTOS = "/productos";
const API_CARRITO = "/api/carrito";

const contenedorProductos = document.getElementById("productos");
const detalleCarrito = document.getElementById("detalleCarrito");
const campoUsuario = document.getElementById("codUsuario");
const elementoTotal = document.getElementById("total");
const elementoMensaje = document.getElementById("mensaje");

let productos = [];
let carrito = [];

function obtenerUsuario() {
    return campoUsuario.value.trim();
}

function mostrarMensaje(texto, error = false) {
    elementoMensaje.textContent = texto;
    elementoMensaje.classList.toggle("error", error);
}

function formatoMoneda(valor) {
    return "Q " + Number(valor).toLocaleString("es-GT", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

async function solicitar(url, opciones = {}) {
    const respuesta = await fetch(url, opciones);

    if (!respuesta.ok) {
        let mensaje = "Error HTTP " + respuesta.status;

        try {
            const datos = await respuesta.json();
            mensaje = datos.mensaje || datos.error || mensaje;
        } catch {
            // La respuesta no contiene JSON.
        }

        throw new Error(mensaje);
    }

    if (respuesta.status === 204) {
        return null;
    }

    return respuesta.json();
}

/* Cargar el catálogo desde Spring Boot */
/* Cargar el catálogo desde Spring Boot */
async function cargarProductos() {
    contenedorProductos.textContent = "Cargando productos...";

    try {
        productos = await solicitar(API_PRODUCTOS);
        mostrarProductos();
    } catch (error) {
        contenedorProductos.textContent = "No fue posible cargar los productos.";
        mostrarMensaje(error.message, true);
    }
}

/* Dibujar las tarjetas de productos */
/* Dibujar las tarjetas de productos con sus imágenes */
function mostrarProductos() {
    contenedorProductos.replaceChildren();

    if (!productos.length) {
        contenedorProductos.textContent = "No hay productos disponibles.";
        return;
    }

    productos.forEach(producto => {
        const tarjeta = document.createElement("article");
        tarjeta.className = "producto";

        // Imagen del producto
        const imagen = document.createElement("img");
        imagen.src = "/" + String(producto.imagen || "").replace(/^\/+/, "");
        imagen.alt = producto.nombre;
        imagen.className = "imagen-producto";
        imagen.loading = "lazy";

        imagen.addEventListener("error", () => {
            imagen.alt = "Imagen no disponible para " + producto.nombre;
        });

        // Nombre
        const nombre = document.createElement("h3");
        nombre.textContent = producto.nombre;

        // Código
        const codigo = document.createElement("p");
        codigo.className = "codigo";
        codigo.textContent = "Código: " + producto.codigo;

        // Precio
        const precio = document.createElement("p");
        precio.className = "precio";
        precio.textContent = formatoMoneda(producto.precio);

        // Cantidad
        const cantidad = document.createElement("input");
        cantidad.type = "number";
        cantidad.min = "1";
        cantidad.step = "1";
        cantidad.value = "1";
        cantidad.setAttribute(
            "aria-label",
            "Cantidad de " + producto.nombre
        );

        // Botón para agregar al carrito
        const boton = document.createElement("button");
        boton.type = "button";
        boton.textContent = "Agregar al carrito";

        boton.addEventListener("click", async () => {
            const valorCantidad = Number(cantidad.value);

            if (!Number.isSafeInteger(valorCantidad) || valorCantidad < 1) {
                mostrarMensaje(
                    "Ingresa una cantidad entera mayor que cero.",
                    true
                );
                return;
            }

            await agregarProducto(producto.idProducto, valorCantidad);
        });

        tarjeta.append(
            imagen,
            nombre,
            codigo,
            precio,
            cantidad,
            document.createTextNode(" "),
            boton
        );

        contenedorProductos.appendChild(tarjeta);
    });
}
        

/* Consultar el carrito del usuario */
async function cargarCarrito() {
    const usuario = obtenerUsuario();

    if (!usuario) {
        mostrarMensaje("Debes ingresar un código de usuario.", true);
        return;
    }

    detalleCarrito.innerHTML =
        '<tr><td colspan="6">Cargando carrito...</td></tr>';

    try {
        carrito = await solicitar(
            API_CARRITO + "/" + encodeURIComponent(usuario)
        );

        mostrarCarrito();
        mostrarMensaje("Carrito cargado correctamente.");
    } catch (error) {
        carrito = [];
        mostrarCarrito();
        mostrarMensaje(error.message, true);
    }
}

/* Agregar un producto al carrito */
async function agregarProducto(idProducto, cantidad) {
    const usuario = obtenerUsuario();

    if (!usuario) {
        mostrarMensaje("Debes ingresar un código de usuario.", true);
        return;
    }

    try {
        await solicitar(
            API_CARRITO + "/" + encodeURIComponent(usuario) + "/agregar",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    idProducto: idProducto,
                    cantidad: cantidad
                })
            }
        );

        await cargarCarrito();
        mostrarMensaje("Producto agregado correctamente.");
    } catch (error) {
        mostrarMensaje(error.message, true);
    }
}

/* Mostrar los productos y calcular el total */
function mostrarCarrito() {
    detalleCarrito.replaceChildren();

    let total = 0;

    if (!carrito.length) {
        const fila = document.createElement("tr");
        const celda = document.createElement("td");

        celda.colSpan = 6;
        celda.textContent = "El carrito está vacío.";

        fila.appendChild(celda);
        detalleCarrito.appendChild(fila);
        elementoTotal.textContent = formatoMoneda(0);
        return;
    }

    carrito.forEach(item => {
        const fila = document.createElement("tr");

        const subtotal = Number(item.subTotal);
        total += subtotal;

        const nombre = document.createElement("td");
        nombre.textContent = item.nombre;

        const codigo = document.createElement("td");
        codigo.textContent = item.codigo;

        const precio = document.createElement("td");
        precio.textContent = formatoMoneda(item.precio);

        const cantidad = document.createElement("td");
        cantidad.textContent = item.cantidad;

        const celdaSubtotal = document.createElement("td");
        celdaSubtotal.textContent = formatoMoneda(subtotal);

        const celdaAccion = document.createElement("td");
        const botonEliminar = document.createElement("button");

        botonEliminar.type = "button";
        botonEliminar.className = "eliminar";
        botonEliminar.textContent = "Eliminar";

        botonEliminar.addEventListener("click", () => {
            eliminarProducto(item.idProducto);
        });

        celdaAccion.appendChild(botonEliminar);

        fila.append(
            nombre,
            codigo,
            precio,
            cantidad,
            celdaSubtotal,
            celdaAccion
        );

        detalleCarrito.appendChild(fila);
    });

    elementoTotal.textContent = formatoMoneda(total);
}

/* Eliminar un producto específico */
async function eliminarProducto(idProducto) {
    const usuario = obtenerUsuario();

    if (!usuario) {
        mostrarMensaje("Debes ingresar un código de usuario.", true);
        return;
    }

    if (!confirm("¿Deseas eliminar este producto del carrito?")) {
        return;
    }

    try {
        await solicitar(
            API_CARRITO + "/" + encodeURIComponent(usuario) +
            "/producto/" + idProducto,
            { method: "DELETE" }
        );

        await cargarCarrito();
        mostrarMensaje("Producto eliminado correctamente.");
    } catch (error) {
        mostrarMensaje(error.message, true);
    }
}

/* Vaciar todo el carrito */
async function vaciarCarrito() {
    const usuario = obtenerUsuario();

    if (!usuario) {
        mostrarMensaje("Debes ingresar un código de usuario.", true);
        return;
    }

    if (!confirm("¿Estás seguro de que deseas vaciar el carrito?")) {
        return;
    }

    try {
        await solicitar(
            API_CARRITO + "/" + encodeURIComponent(usuario),
            { method: "DELETE" }
        );

        await cargarCarrito();
        mostrarMensaje("El carrito se vació correctamente.");
    } catch (error) {
        mostrarMensaje(error.message, true);
    }
}

/* Eventos de los botones */
document.getElementById("btnCargar").addEventListener("click", cargarCarrito);

document.getElementById("btnVaciar").addEventListener("click", vaciarCarrito);

/* Inicializar la página */
cargarProductos();
cargarCarrito();
