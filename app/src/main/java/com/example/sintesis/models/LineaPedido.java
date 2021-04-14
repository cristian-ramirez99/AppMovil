package com.example.sintesis.models;

public class LineaPedido {
    private int cantidadad;
    private Producto producto;
    private Pedido string;
    private String _id;


    public LineaPedido(int cantidadad, Producto producto, Pedido string, String _id) {
        this.cantidadad = cantidadad;
        this.producto = producto;
        this.string = string;
        this._id = _id;
    }

    public int getCantidadad() {
        return cantidadad;
    }

    public void setCantidadad(int cantidadad) {
        this.cantidadad = cantidadad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getString() {
        return string;
    }

    public void setString(Pedido string) {
        this.string = string;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}

