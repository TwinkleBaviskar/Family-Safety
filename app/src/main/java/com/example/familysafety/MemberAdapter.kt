import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familysafety.ContactModel
import com.example.familysafety.R

class MemberAdapter(
    private var memberList: MutableList<ContactModel>,
    private val onDeleteClick: (ContactModel) -> Unit
) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val batteryPercent: TextView = itemView.findViewById(R.id.battery_percent)
        val batteryIcon: ImageView = itemView.findViewById(R.id.battery)
        val deleteIcon: ImageView = itemView.findViewById(R.id.remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val context = holder.itemView.context
        val member = memberList[position]

        holder.name.text = member.name
        holder.batteryPercent.text = "${getBatteryPercentage(context)}%"
        holder.batteryIcon.setImageResource(R.drawable.battery)

        holder.deleteIcon.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDeleteClick(member)
            }
        }
    }

    override fun getItemCount() = memberList.size

    fun updateList(newList: List<ContactModel>) {
        memberList = newList.toMutableList()
        notifyDataSetChanged()
    }

    private fun getBatteryPercentage(context: Context): Int {
        val batteryStatus = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        return if (level >= 0 && scale > 0) (level * 100) / scale else 0
    }
}
