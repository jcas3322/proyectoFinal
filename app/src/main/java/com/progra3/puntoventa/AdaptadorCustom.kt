package com.progra3.puntoventa

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorCustom(var contexto:Context,items:ArrayList<Elementos>):RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {

    var items:ArrayList<Elementos>? =null

    init {
        this.items=items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorCustom.ViewHolder {
        val vista=LayoutInflater.from(contexto).inflate(R.layout.template_inventario,parent,false)
        val viewholder=ViewHolder(vista)
        return viewholder
    }

    override fun onBindViewHolder(holder: AdaptadorCustom.ViewHolder, position: Int) {
        val item=items?.get(position)
        val uri=Uri.parse(item?.url_imagen)
        val stream=contexto.contentResolver.openInputStream(uri)
        val imageBitmap=BitmapFactory.decodeStream(stream)
        holder.foto?.setImageBitmap(imageBitmap)
        holder.nombre?.text="NOMBRE: "+item?.nombre
        holder.id_articulo?.text="ID: "+item?.id_elemento.toString()
        holder.codigo_articulo?.text="COD: "+item?.codigo
        holder.precio_venta?.text="Q."+item?.precio_venta.toString()
        holder.precio_compra?.text="Q."+item?.precio_compra.toString()
        holder.stock?.text="STOCK: "+item?.stock.toString()
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    class ViewHolder(vista:View):RecyclerView.ViewHolder(vista){
        var vista=vista
        var foto:ImageView?=null
        var id_articulo:TextView?=null
        var codigo_articulo:TextView?=null
        var precio_compra:TextView?=null
        var precio_venta:TextView?=null
        var nombre:TextView?=null
        var stock:TextView?=null

        init{
            foto=vista.findViewById(R.id.imagenRecicler)
            id_articulo=vista.findViewById(R.id.idProducto)
            codigo_articulo=vista.findViewById(R.id.codigoProducto)
            precio_compra=vista.findViewById(R.id.precioCompra)
            precio_venta=vista.findViewById(R.id.precioVenta)
            nombre=vista.findViewById(R.id.nombreProducto)
            stock=vista.findViewById(R.id.stockProducto)
        }
    }
}