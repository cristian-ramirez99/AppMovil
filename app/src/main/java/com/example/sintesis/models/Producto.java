package com.example.sintesis.models;


import com.example.sintesis.environments.Environments;

public class Producto {
    private String nombre;
    private String descripcion;
    private double precio;
    private String img;
    private int stock;
    private String _id;

    public class TipoProducto {
        private String nombre;
        public String caracteristicas;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Producto(String nombre, String descripcion, double precio, String img, int stock, String _id) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.img = img;
        this.stock = stock;
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImg() {
        if (img.isEmpty()) {
            return Environments.BASE_URL + "upload/usuarios/no-image";
        } else {
            return Environments.BASE_URL + "uploads/productos/" + img;
        }
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }
}
