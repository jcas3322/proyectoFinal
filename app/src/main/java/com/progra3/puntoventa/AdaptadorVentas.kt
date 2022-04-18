package com.progra3.puntoventa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progra3.Interfaces.ClickListener
import com.progra3.modelos.Articulo

class AdaptadorVentas(var item:ArrayList<Articulo>, var listener:ClickListener): RecyclerView.Adapter<AdaptadorVentas.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorVentas.ViewHolder {
        val vista=LayoutInflater.from(parent.context).inflate(R.layout.layout_ventas,parent,false)
        val viewholder=ViewHolder(vista, listener)
        return viewholder
    }

    override fun onBindViewHolder(holder: AdaptadorVentas.ViewHolder, position: Int) {
        val info=item.get(position)
        holder.textoNombre.setText(info.nombre)
        holder.textoGeneral.setText("Q"+info.precioVenta+" #"+info.stock+" Q"+(info.precioVenta*info.stock))
    }

    override fun getItemCount(): Int =item.count()

    class ViewHolder(view:View, listener: ClickListener):RecyclerView.ViewHolder(view), View.OnClickListener{

        var textoNombre:TextView
        var textoGeneral:TextView
        var vista:View
        var listener: ClickListener

        init {
            this.vista=view
            this.textoNombre=vista.findViewById(R.id.nombreProductoventas)
            this.textoGeneral=vista.findViewById(R.id.generalInfoventas)
            this.listener=listener
            this.vista.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            this.listener.conClick(p0!!,bindingAdapterPosition)
        }
    }
}