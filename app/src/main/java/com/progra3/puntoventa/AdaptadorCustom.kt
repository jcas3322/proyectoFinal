package com.progra3.puntoventa

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.progra3.Interfaces.ClickListener
import com.progra3.modelos.Articulo
import com.squareup.picasso.Picasso

class AdaptadorCustom(var contexto:Context,item:ArrayList<Articulo>, var listener: ClickListener):RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {

    var items:ArrayList<Articulo>?=null

    init {
        this.items=item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorCustom.ViewHolder {
        val vista=LayoutInflater.from(parent.context).inflate(R.layout.template_inventario,parent,false)
        val viewholder=ViewHolder(vista, listener)
        return viewholder
    }

    override fun onBindViewHolder(holder: AdaptadorCustom.ViewHolder, position: Int) {
        val item=items?.get(position)
        val uri=Uri.parse(item?.urlImagen)
        //val stream=contexto.contentResolver.openInputStream(uri)
        //val imageBitmap=BitmapFactory.decodeStream(stream)
        //holder.foto?.setImageBitmap(imageBitmap)
        Picasso.with(contexto).load(uri).into(holder.foto) //Conociendo a Picasso
        holder.nombre?.text=item?.nombre
        holder.codigo_articulo?.text=item?.codigo
        holder.precio_venta?.text="Q."+item?.precioVenta.toString()
        holder.precio_compra?.text="Q."+item?.precioCompra.toString()
        holder.stock?.text="#"+item?.stock.toString()
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    class ViewHolder(vista:View, listener: ClickListener):RecyclerView.ViewHolder(vista), View.OnClickListener{
        var vista=vista
        var foto:ImageView?=null
        var codigo_articulo:TextView?=null
        var precio_compra:TextView?=null
        var precio_venta:TextView?=null
        var nombre:TextView?=null
        var stock:TextView?=null
        var listener:ClickListener?=null

        init{
            //cardview=vista.findViewById(R.id.pricipal)
            foto=vista.findViewById(R.id.imagenRecicler)
            codigo_articulo=vista.findViewById(R.id.codigoProducto)
            precio_compra=vista.findViewById(R.id.precioCompra)
            precio_venta=vista.findViewById(R.id.precioVenta)
            nombre=vista.findViewById(R.id.nombreProducto)
            stock=vista.findViewById(R.id.stockProducto)
            this.listener=listener
            this.vista?.setOnClickListener(this)
            //this.foto?.setOnClickListener(this)
            //this.foto?.tag="SOYFOTO"
            //this.vista?.tag="SOYMAIN"
        }

        override fun onClick(p0: View?) {
            this.listener?.conClick(p0!!,bindingAdapterPosition)
        }
    }
}