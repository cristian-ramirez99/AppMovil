package com.example.sintesis.models;


public class Producto {
    public String nombre;
    public String descripcion;
    public double precio;
    public String img;
    public TipoProducto tipoProducto;
    public String id;
    public int cantidad;

    public class TipoProducto {
        private String nombre;
        public String caracteristicas;
    }
}
