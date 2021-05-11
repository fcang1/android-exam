package com.example.upraxisexam.presentation.personlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.upraxisexam.data.database.PersonEntity
import com.example.upraxisexam.databinding.ItemPersonBinding

class PersonListAdapter(
        private val personListListener: PersonListListener
) : ListAdapter<PersonEntity, RecyclerView.ViewHolder>(PersonListDiffCallback()) {

    class ItemPersonViewHolder private constructor(
            private val binding: ItemPersonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            const val VIEW_TYPE = 0

            fun from(parent: ViewGroup) = ItemPersonViewHolder(
                    ItemPersonBinding.inflate(
                            LayoutInflater.from(
                                    parent.context
                            ),
                            parent,
                            false
                    )
            )
        }

        fun bind(personEntity: PersonEntity, personListListener: PersonListListener) {
            binding.personEntity = personEntity
            binding.personListListener = personListListener
            binding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int) = ItemPersonViewHolder.VIEW_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ItemPersonViewHolder.VIEW_TYPE -> ItemPersonViewHolder.from(parent)
        else -> throw IllegalArgumentException("Unknown view type")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemPersonViewHolder -> holder.bind(getItem(position), personListListener)
            else -> throw IllegalArgumentException("Unknown ViewHolder class")
        }
    }
}

private class PersonListDiffCallback : DiffUtil.ItemCallback<PersonEntity>() {
    override fun areItemsTheSame(oldItem: PersonEntity, newItem: PersonEntity) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PersonEntity, newItem: PersonEntity) = oldItem == newItem
}

class PersonListListener(val clickListener: (personEntity: PersonEntity) -> Unit) {
    fun onClick(personEntity: PersonEntity) = clickListener(personEntity)
}