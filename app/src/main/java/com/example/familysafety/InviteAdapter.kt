package com.example.familysafety

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InviteAdapter(
        private var contactList: List<ContactModel>,
        private val onInviteClick: (ContactModel) -> Unit
) : RecyclerView.Adapter<InviteAdapter.InviteViewHolder>() {

        inner class InviteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val name: TextView = itemView.findViewById(R.id.name)
                val invite: TextView = itemView.findViewById(R.id.invite)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_invite, parent, false)
                return InviteViewHolder(view)
        }

        override fun onBindViewHolder(holder: InviteViewHolder, position: Int) {
                val contact = contactList[position]
                holder.name.text = contact.name
                holder.invite.setOnClickListener { onInviteClick(contact)
                }
        }

        override fun getItemCount() = contactList.size

        fun updateList(newList: List<ContactModel>) {
                contactList = newList
                notifyDataSetChanged()
        }
}
