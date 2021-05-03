package com.example.sintesis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sintesis.environments.Environments;
import com.example.sintesis.models.LineaPedido;

import java.text.DecimalFormat;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private final String SIMBOLO_EURO = "\u20ac";

    private LineaPedido lineaPedidos[];
    private ImageView ivBasura;


    final ListaAdapter.OnIconBasuraClickListener listener;

    //Creamos interface OnClick para poder gestionarlo en CarritoFragment
    public interface OnIconBasuraClickListener {
        void onClick(LineaPedido lineaPedido);
    }

    public ListaAdapter(LineaPedido[] itemList, ListaAdapter.OnIconBasuraClickListener listener) {
        this.lineaPedidos = itemList;

        //Pasamos el listener OnClick
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return lineaPedidos.length;
    }

    @Override
    public ListaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_productos, null, false);

        //Obtener referencia del widget
        ivBasura = view.findViewById(R.id.ivBasuraListaProductos);

        return new ListaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListaAdapter.ViewHolder holder, final int position) {
        holder.bindData(lineaPedidos[position]);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView nombreYCantidad, precioYPrecioTotal;

        ViewHolder(View itemView) {
            super(itemView);

            //Obtener referencia de los widgets
            iconImage = itemView.findViewById(R.id.ivProductoListaProductos);
            nombreYCantidad = itemView.findViewById(R.id.tvNombreYCantidadListaProductos);
            precioYPrecioTotal = itemView.findViewById(R.id.tvPrecioListaProductos);
        }

        void bindData(final LineaPedido item) {
            //setImagenProducto
            String url = item.getProducto().getImg();
            Glide.with(itemView.getContext()).load(url).into(iconImage);

            //setNombreYCantidad
            nombreYCantidad.setText(item.getProducto().getNombre() + " (" + item.getCantidadad() + ")");

            //Calculamos el subtotal por producto
            Double precioTotalProducto = calcularPrecioTotalProducto(item.getProducto().getPrecio(), item.getCantidadad());

            //Formato con dos decimales
            DecimalFormat format = new DecimalFormat("#.00");// el numero de ceros despues del entero
            String strPrecio = format.format(item.getProducto().getPrecio());
            String strPrecioTotalProducto = format.format(precioTotalProducto);

            //setPrecioYPrecioTotal
            precioYPrecioTotal.setText(strPrecio + SIMBOLO_EURO + " - Total: " + strPrecioTotalProducto + SIMBOLO_EURO);

            //OnClick del ImageView que tiene icono de basura
            ivBasura.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }
    }

    //Devuelve la facturacion de un producto por pedido
    private double calcularPrecioTotalProducto(Double precio, int canitdad) {
        return precio * canitdad;
    }
}

