package com.example.sintesis.models;


public class Producto {
    private String base_url = "http://10.0.2.2:3000/api/";
    public String nombre;
    public String descripcion;
    public double precio;
    public String img;
    public String id;

    public class TipoProducto {
        private String nombre;
        public String caracteristicas;
    }

    public Producto(String nombre, String descripcion, double precio, String img, String id) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.img = img;
        this.id = id;
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
            return base_url + "upload/usuarios/no-image.jpg";
        } else {
            return base_url + "upload/usuarios/" + img;
        }
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
