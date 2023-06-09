package com.example.gradapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gradapp.models.CorrListItem
import org.json.JSONArray


class CorrelationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.name)
    val corrParameter: TextView = view.findViewById(R.id.corrParameter)
    val filteredData: TextView = view.findViewById(R.id.filteredData)
    val showDataButton: Button = view.findViewById(R.id.showDataButton)  // Add this line
}
class CorrelationAdapter(private val list: List<CorrListItem>) : RecyclerView.Adapter<CorrelationViewHolder>() {

    // Keep track of visibility state
    private val visibilityState = HashMap<Int, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CorrelationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CorrelationViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CorrelationViewHolder, position: Int) {
        val item = list[position]

        holder.name.text = item.name
        holder.corrParameter.text = item.corrParameter

        // Parse JSON string to JSONArray
        val jsonArray = JSONArray(item.filteredData)

        val prettyPrintJson = jsonArray.toString(4) // 4 is the number of spaces for indentation


        holder.filteredData.text = prettyPrintJson

        // Restore visibility state
        holder.filteredData.visibility = if (visibilityState[position] == true) View.VISIBLE else View.GONE

        // Button click listener
        holder.showDataButton.setOnClickListener {
            // Toggle visibility state
            if (visibilityState[position] == true) {
                visibilityState[position] = false
                holder.filteredData.visibility = View.GONE
            } else {
                visibilityState[position] = true
                holder.filteredData.visibility = View.VISIBLE
            }
        }
    }
}
